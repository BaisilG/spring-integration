/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.amqp.config;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mark Fisher
 * @since 2.1
 */
public class PubSubChannelSample {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("PubSubChannelSample-context.xml", PubSubChannelSample.class);
	}

}
