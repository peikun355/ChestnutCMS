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
package com.chestnut.seo.publishpipe;

import com.chestnut.contentcore.core.IPublishPipeProp;
import com.chestnut.seo.fixed.dict.SitemapPageType;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 发布通道属性：站点地图页面类型
 *
 * @author 兮玥
 * @email 190785909@qq.com
 */
@Component
public class PublishPipeProp_SitemapPageType implements IPublishPipeProp {
	
	public static final String KEY = "sitemapPageType";

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName() {
		return "站点地图页面类型";
	}

	@Override
	public List<PublishPipePropUseType> getUseTypes() {
		return List.of(PublishPipePropUseType.Site);
	}

	public static String getValue(String publishPipeCode, Map<String, Map<String, Object>> publishPipeProps) {
		if (Objects.nonNull(publishPipeProps)) {
			return MapUtils.getString(publishPipeProps.get(publishPipeCode), KEY, SitemapPageType.PC);
		}
		return SitemapPageType.PC;
	}
}
