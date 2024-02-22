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
package com.chestnut.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chestnut.common.db.domain.LogicDeleteEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章详情表对象 [cms_article_detail]
 * 
 * @author 兮玥
 * @email 190785909@qq.com
 */
@Getter
@Setter
@TableName(CmsArticleDetail.TABLE_NAME)
public class CmsArticleDetail extends LogicDeleteEntity {

    public static final String TABLE_NAME = "cms_article_detail";

    /**
     * 内容ID
     */
    @TableId(value = "content_id", type = IdType.INPUT)
    private Long contentId;
    
    /**
     * 站点ID
     */
    private Long siteId;
    
    /**
     * 正文详情（json）
     */
    private String contentJson;

    /**
     * 正文详情（html）
     */
    private String contentHtml;

    /**
     * 分页标题
     */
    private String pageTitles;

    /**
     * 是否下载远程图片
     */
    private String downloadRemoteImage;
}
