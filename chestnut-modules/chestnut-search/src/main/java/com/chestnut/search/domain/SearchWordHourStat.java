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
package com.chestnut.search.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 搜索词小时统计数据
 *
 * @author 兮玥
 * @email 190785909@qq.com
 */
@Getter
@Setter
@TableName(SearchWordHourStat.TABLE_NAME)
public class SearchWordHourStat implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;

	public final static String TABLE_NAME = "search_word_hour_stat";

	@TableId(value = "stat_id", type = IdType.AUTO)
	private Long statId;
	
	/**
	 * 统计周期，格式：yyyyMMddHH
	 */
	private String hour;
	
	/**
	 * 搜索词ID
	 */
	private Long wordId;

	/**
	 * 搜索词（冗余）
	 */
	private String word;
	
	/**
	 * 检索次数
	 */
	private Integer searchCount;
}
