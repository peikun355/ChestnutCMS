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
package com.chestnut.media.domain.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.chestnut.common.utils.StringUtils;
import com.chestnut.contentcore.domain.CmsContent;
import com.chestnut.contentcore.domain.dto.ContentDTO;
import com.chestnut.contentcore.fixed.dict.ContentAttribute;
import com.chestnut.contentcore.util.InternalUrlUtils;
import com.chestnut.media.domain.CmsVideo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoAlbumDTO extends ContentDTO {

	private List<CmsVideo> videoList;

	public static VideoAlbumDTO newInstance(CmsContent content, List<CmsVideo> videoList) {
		VideoAlbumDTO dto = new VideoAlbumDTO();
		BeanUtils.copyProperties(content, dto);
		dto.setAttributes(ContentAttribute.convertStr(content.getAttributes()));
		if (StringUtils.isNotEmpty(dto.getLogo())) {
			dto.setLogoSrc(InternalUrlUtils.getActualPreviewUrl(dto.getLogo()));
		}
    	dto.setVideoList(videoList);
		return dto;
	}
}
