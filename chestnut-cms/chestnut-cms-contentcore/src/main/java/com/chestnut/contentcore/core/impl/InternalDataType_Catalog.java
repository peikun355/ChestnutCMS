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
package com.chestnut.contentcore.core.impl;

import java.io.IOException;

import com.chestnut.contentcore.util.CatalogUtils;
import com.chestnut.system.fixed.dict.YesOrNo;
import org.springframework.stereotype.Component;

import com.chestnut.contentcore.core.IInternalDataType;
import com.chestnut.contentcore.core.InternalURL;
import com.chestnut.contentcore.domain.CmsCatalog;
import com.chestnut.contentcore.service.ICatalogService;
import com.chestnut.contentcore.service.IPublishService;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

/**
 * 内部数据类型：栏目
 *
 * @author 兮玥
 * @email 190785909@qq.com
 */
@RequiredArgsConstructor
@Component(IInternalDataType.BEAN_NAME_PREFIX + InternalDataType_Catalog.ID)
public class InternalDataType_Catalog implements IInternalDataType {

	public final static String ID = "catalog";
	
	private final ICatalogService catalogService;
	
	private final IPublishService publishService;
	
	@Override
	public String getId() {
		return ID;
	}
	
	@Override
	public String getPageData(RequestData requestData) throws IOException, TemplateException {
		CmsCatalog catalog = catalogService.getCatalog(requestData.getDataId());
		boolean listFlag = YesOrNo.isYes(requestData.getParams().get("list"));
		return this.publishService.getCatalogPageData(catalog, requestData.getPageIndex(), listFlag, requestData.getPublishPipeCode(), requestData.isPreview());
	}

	@Override
	public String getLink(InternalURL internalUrl, int pageIndex, String publishPipeCode, boolean isPreview) {
		CmsCatalog catalog = catalogService.getCatalog(internalUrl.getId());
		return this.catalogService.getCatalogLink(catalog, pageIndex, publishPipeCode, isPreview);
	}
}
