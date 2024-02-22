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

import java.util.Objects;

public class XssContextHolder {

	private static final ThreadLocal<Boolean> CONTEXT = new ThreadLocal<>();
	
	/**
	 * 默认为true
	 */
	public static boolean isIgnore() {
		return Objects.isNull(CONTEXT.get()) ? true : CONTEXT.get();
	}
	
	public static void ignore() {
		CONTEXT.set(true);
	}
	
	public static void remove() {
		CONTEXT.remove();
	}
}
