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
package com.chestnut.system.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "chestnut.system")
public class SysProperties {

	/**
	 * 资源文件上传根目录
	 */
	private String uploadPath;
	
	/**
	 * 后台登录注册验证码类型配置
	 */
	private String captchaType;
	
	/** 演示模式开关 */
	private boolean demoMode;

	/**
	 * 是否记录定时任务日志到数据库
	 */
	private boolean scheduleLog;
}
