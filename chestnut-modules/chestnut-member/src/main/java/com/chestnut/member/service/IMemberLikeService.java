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
package com.chestnut.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.member.domain.MemberLike;

import java.util.List;

public interface IMemberLikeService extends IService<MemberLike> {

    /**
     * 点赞
     *
     * @param memberId
     * @param dataType
     * @param dataId
     */
    void like(Long memberId, String dataType, Long dataId);

    /**
     * 取消点赞
     *
     * @param memberId
     * @param dataType
     * @param dataId
     */
    void cancelLike(Long memberId, String dataType, Long dataId);

    List<MemberLike> getMemberLikes(Long memberId, String dataType, Integer limit, Long offset);
}