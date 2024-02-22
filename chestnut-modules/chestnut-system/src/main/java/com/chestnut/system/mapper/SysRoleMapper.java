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
package com.chestnut.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.system.domain.SysRole;
import com.chestnut.system.domain.SysUserRole;

/**
 * 角色表 数据层
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userName 用户名
	 * @return 角色列表
	 */
	@Select("SELECT r.* FROM " + SysUserRole.TABLE_NAME + " ur LEFT JOIN " + SysRole.TABLE_NAME
			+ " r ON ur.role_id = r.role_id WHERE ur.user_id = #{userId}")
	public List<SysRole> selectRolesByUserId(Long userId);
}
