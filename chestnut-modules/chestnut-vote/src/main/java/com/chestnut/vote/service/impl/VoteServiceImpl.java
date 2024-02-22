/*
 * Copyright 2022-2024 兮玥(190785909@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chestnut.vote.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.common.exception.CommonErrorCode;
import com.chestnut.common.redis.RedisCache;
import com.chestnut.common.utils.Assert;
import com.chestnut.common.utils.IdUtils;
import com.chestnut.vote.core.IVoteItemType;
import com.chestnut.vote.core.IVoteUserType;
import com.chestnut.vote.domain.Vote;
import com.chestnut.vote.domain.VoteLog;
import com.chestnut.vote.domain.VoteSubject;
import com.chestnut.vote.domain.VoteSubjectItem;
import com.chestnut.vote.domain.vo.VoteSubjectItemVO;
import com.chestnut.vote.domain.vo.VoteSubjectVO;
import com.chestnut.vote.domain.vo.VoteVO;
import com.chestnut.vote.exception.VoteErrorCode;
import com.chestnut.vote.fixed.dict.VoteSubjectType;
import com.chestnut.vote.listener.BeforeVoteAddEvent;
import com.chestnut.vote.mapper.VoteLogMapper;
import com.chestnut.vote.mapper.VoteMapper;
import com.chestnut.vote.mapper.VoteSubjectItemMapper;
import com.chestnut.vote.mapper.VoteSubjectMapper;
import com.chestnut.vote.service.IVoteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements IVoteService, ApplicationContextAware {

	private static final String CACHE_PREFIX = "vote:";

	private ApplicationContext applicationContext;
	
	private final VoteMapper voteMapper;

	private final VoteSubjectMapper subjectMapper;

	private final VoteSubjectItemMapper itemMapper;

	private final VoteLogMapper voteLogMapper;

	private final RedisCache redisCache;
	
	private final Map<String, IVoteUserType> voteUserTypes;
	
	private final Map<String, IVoteItemType> voteItemTypes;
	
	private final RedissonClient redissonClient;
	
	/**
	 * 获取问卷调查参与用户类型
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public IVoteUserType getVoteUserType(String type) {
		IVoteUserType vut = this.voteUserTypes.get(IVoteUserType.BEAN_PREFIX + type);
		Assert.notNull(vut, () -> VoteErrorCode.UNSUPPORTED_VOTE_USER_TYPE.exception(type));
		return vut;
	}
	
	/**
	 * 获取问卷调查选项类型
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public IVoteItemType getVoteItemType(String type) {
		IVoteItemType vut = this.voteItemTypes.get(IVoteItemType.BEAN_PREFIX + type);
		Assert.notNull(vut, () -> VoteErrorCode.UNSUPPORTED_VOTE_ITEM_TYPE.exception(type));
		return vut;
	}

	/**
	 * 获取问卷调查详情
	 * 
	 * @param voteId
	 * @return
	 */
	@Override
	public VoteVO getVote(Long voteId) {
		return this.redisCache.getCacheObject(CACHE_PREFIX + voteId, () -> loadVoteDetail(voteId));
	}

	private VoteVO loadVoteDetail(Long voteId) {
		Vote vote = this.getById(voteId);
		Assert.notNull(vote, () -> CommonErrorCode.DATA_NOT_FOUND_BY_ID.exception("voteId", voteId));

		VoteVO voteVO = new VoteVO();
		voteVO.setVoteId(vote.getVoteId());
		voteVO.setTitle(vote.getTitle());
		voteVO.setStartTime(vote.getStartTime());
		voteVO.setEndTime(vote.getEndTime());
		voteVO.setUserType(vote.getUserType());
		voteVO.setDayLimit(vote.getDayLimit());
		voteVO.setTotalLimit(vote.getTotalLimit());
		voteVO.setViewType(vote.getViewType());
		voteVO.setTotal(voteVO.getTotal());

		Map<Long, List<VoteSubjectItemVO>> itemMap = this.itemMapper
				.selectList(new LambdaQueryWrapper<VoteSubjectItem>().eq(VoteSubjectItem::getVoteId, vote.getVoteId())
						.orderByAsc(VoteSubjectItem::getSortFlag))
				.stream().map(item -> {
					VoteSubjectItemVO vo = new VoteSubjectItemVO();
					vo.setItemId(item.getItemId());
					vo.setSubjectId(item.getSubjectId());
					vo.setType(item.getType());
					vo.setContent(item.getContent());
					vo.setDescription(item.getDescription());
					vo.setSortFlag(item.getSortFlag());
					return vo;
				}).collect(Collectors.groupingBy(VoteSubjectItemVO::getSubjectId));

		List<VoteSubjectVO> subjects = this.subjectMapper.selectList(new LambdaQueryWrapper<VoteSubject>()
				.eq(VoteSubject::getVoteId, vote.getVoteId()).orderByAsc(VoteSubject::getSortFlag)).stream()
				.map(subject -> {
					VoteSubjectVO voteSubjectVO = new VoteSubjectVO();
					voteSubjectVO.setSubjectId(subject.getSubjectId());
					voteSubjectVO.setTitle(subject.getTitle());
					voteSubjectVO.setType(subject.getType());
					voteSubjectVO.setSortFlag(subject.getSortFlag());
					voteSubjectVO.setItems(itemMap.getOrDefault(subject.getSubjectId(), List.of()));
					return voteSubjectVO;
				}).toList();
		voteVO.setSubjects(subjects);
		return voteVO;
	}

	/**
	 * 删除指定问卷调查缓存
	 * 
	 * @param voteId
	 */
	@Override
	public void clearVoteCache(Long voteId) {
		this.redisCache.deleteObject(CACHE_PREFIX + voteId);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void addVote(Vote vote) {
		this.checkUnique(vote);

		vote.setVoteId(IdUtils.getSnowflakeId());
		vote.setTotal(0);

		this.applicationContext.publishEvent(new BeforeVoteAddEvent(this, vote));
		this.save(vote);
	}

	@Override
	public void updateVote(Vote vote) {
		Vote db = this.getById(vote.getVoteId());
		Assert.notNull(db, () -> CommonErrorCode.DATA_NOT_FOUND_BY_ID.exception("voteId", vote.getVoteId()));

		this.checkUnique(db);

		db.setTitle(vote.getTitle());
		db.setCode(vote.getCode());
		db.setUserType(null);
		db.setDayLimit(vote.getDayLimit());
		db.setTotalLimit(vote.getTotalLimit());
		db.setViewType(vote.getViewType());
		db.setStartTime(vote.getStartTime());
		db.setEndTime(vote.getEndTime());
		db.setStatus(vote.getStatus());
		db.setRemark(vote.getRemark());
		this.updateById(db);
		// 更新缓存
		this.clearVoteCache(db.getVoteId());
	}

	private void checkUnique(Vote vote) {
		boolean unique = this.lambdaQuery().eq(Vote::getCode, vote.getCode())
				.ne(IdUtils.validate(vote.getVoteId()), Vote::getVoteId, vote.getVoteId()).count() == 0;
		Assert.isTrue(unique, () -> CommonErrorCode.DATA_CONFLICT.exception("code"));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteVotes(List<Long> voteIds) {
		this.removeByIds(voteIds);
		// 删除问卷调查主题
		this.subjectMapper.delete(new LambdaQueryWrapper<VoteSubject>().in(VoteSubject::getVoteId, voteIds));
		// 删除问卷调查主题选项
		this.itemMapper.delete(new LambdaQueryWrapper<VoteSubjectItem>().in(VoteSubjectItem::getVoteId, voteIds));
		// 删除问卷调查相关日志
		this.voteLogMapper.delete(new LambdaQueryWrapper<VoteLog>().in(VoteLog::getVoteId, voteIds));
		// 更新缓存
		voteIds.forEach(voteId -> this.clearVoteCache(voteId));
	}

	/**
	 * 更新问卷参与数和主题选项票数
	 * 
	 * @param voteLog
	 * @param vote
	 */
	public void onVoteSubmit(VoteLog voteLog) {
		RLock lock = redissonClient.getLock("VoteTotalUpdate-" + voteLog.getVoteId());
		lock.lock();
		try {
			VoteVO vote = this.getVote(voteLog.getVoteId());
			// 问卷调查参与数+1
			this.voteMapper.incrVoteTotal(voteLog.getVoteId());
			// 单选/多选主题选项票数+1
			vote.getSubjects().forEach(subject -> {
				if (!VoteSubjectType.isInput(subject.getType())) {
					String result = voteLog.getResult().get(subject.getSubjectId());
					this.itemMapper.incrItemTotal(Long.valueOf(result));
				}
			});
		} finally {
			lock.unlock();
		}
	}
}