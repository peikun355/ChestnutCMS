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
package com.chestnut.common.staticize.tag;

import java.util.List;

public interface ITag {
	
    /**
     * 标签名
     * 
     * <@value></@value>
     */
    public String getTagName();

    /**
     * 标签名称
     */
    public String getName();

    /**
     * 标签描述
     */
    default public String getDescription() {
        return "";
    }
    
    /**
     * 标签属性定义
     */
    default public List<TagAttr> getTagAttrs() {
    	return null;
    }
}
