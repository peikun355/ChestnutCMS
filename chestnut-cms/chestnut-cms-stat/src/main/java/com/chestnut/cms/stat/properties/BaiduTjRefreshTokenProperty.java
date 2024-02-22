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
package com.chestnut.cms.stat.properties;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.chestnut.contentcore.core.IProperty;
import com.chestnut.contentcore.util.ConfigPropertyUtils;

@Component(IProperty.BEAN_NAME_PREFIX + BaiduTjRefreshTokenProperty.ID)
public class BaiduTjRefreshTokenProperty implements IProperty {

	public final static String ID = "BaiduTjRefreshToken";

	static UseType[] UseTypes = new UseType[] { UseType.Site };

	@Override
	public UseType[] getUseTypes() {
		return UseTypes;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return "百度统计RefreshToken";
	}
	
	@Override
	public boolean isSensitive() {
		return true;
	}

	public static String getValue(Map<String, String> siteConfigProps) {
		return ConfigPropertyUtils.getStringValue(ID, siteConfigProps);
	}
}
