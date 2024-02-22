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

import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.system.domain.SysUserRole;

/**
 * 用户与角色关联表 数据层
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

	/**
	 * 批量新增用户角色信息
	 * 
	 * @param userRoleList
	 *            用户角色列表
	 * @return 结果
	 */
	@Insert("""
			<script>
				insert into sys_user_role(user_id, role_id) values
				<foreach item="item" index="index" collection="list" separator=",">
					(#{item.userId},#{item.roleId})
				</foreach>
			</script>
			""")
	public int batchInserUserRoles(List<SysUserRole> userRoleList);
}
