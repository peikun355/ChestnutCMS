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
package com.chestnut.generator.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.generator.domain.GenTableColumn;

/**
 * 业务字段 数据层
 *
 * @author 兮玥
 * @email 190785909@qq.com
 */
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {
	
	/**
	 * 根据表名称查询列信息
	 * 
	 * @param tableName
	 *            表名称
	 * @return 列信息
	 */
	public List<GenTableColumn> selectDbTableColumnsByName(String tableName);

	/**
	 * 查询业务字段列表
	 * 
	 * @param tableId
	 *            业务字段编号
	 * @return 业务字段集合
	 */
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId);

	/**
	 * 新增业务字段
	 * 
	 * @param genTableColumn
	 *            业务字段信息
	 * @return 结果
	 */
	public int insertGenTableColumn(GenTableColumn genTableColumn);

	/**
	 * 修改业务字段
	 * 
	 * @param genTableColumn
	 *            业务字段信息
	 * @return 结果
	 */
	public int updateGenTableColumn(GenTableColumn genTableColumn);
}
