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
package com.chestnut.word.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.common.domain.TreeNode;
import com.chestnut.word.domain.TagWordGroup;

public interface ITagWordGroupService extends IService<TagWordGroup> {
	
	/**
	 * 添加TAG词分组
	 * 
	 * @param group
	 * @return
	 */
	TagWordGroup addTagWordGroup(TagWordGroup group);

	/**
	 * 编辑TAG词分组
	 * 
	 * @param group
	 * @return
	 */
	void editTagWordGroup(TagWordGroup group);

	/**
	 * 删除TAG词分组
	 * 
	 * @param groupIds
	 * @return
	 */
	void deleteTagWordGroups(List<Long> groupIds);

	/**
	 * 生成分组树数据
	 * 
	 * @param groups
	 * @return
	 */
	List<TreeNode<String>> buildTreeData(List<TagWordGroup> groups);
}
