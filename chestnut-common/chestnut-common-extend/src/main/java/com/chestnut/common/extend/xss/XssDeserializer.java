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
package com.chestnut.common.extend.xss;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.chestnut.common.extend.enums.XssMode;
import com.chestnut.common.utils.HtmlUtils;
import com.chestnut.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class XssDeserializer extends JsonDeserializer<String> {

	private final XssMode mode;
	
	@Override
	public LogicalType logicalType() {
		return LogicalType.Textual;
	}
	
	@Override
	public String getNullValue(DeserializationContext ctxt) throws JsonMappingException {
		return StringUtils.EMPTY;
	}
	
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            return deal(p.getText());
        }
        JsonToken token = p.getCurrentToken();
		if (token.isScalarValue()) {
            String text = p.getValueAsString();
            if (text != null) {
                return text;
            }
		}
        return (String) ctxt.handleUnexpectedToken(String.class, p);
	}
	
	private String deal(String text) {
		if (Objects.nonNull(text) && !XssContextHolder.isIgnore()) {
			if (mode == XssMode.CLEAN) {
				text = HtmlUtils.clean(text);
			} else {
				text = HtmlUtils.escape(text);
			}
		}
		return text;
	}
}