/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.training.multiservicewebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
				.nullDestMatcher().authenticated()
				.simpSubscribeDestMatchers("/user/queue/errors").permitAll()
                .simpSubscribeDestMatchers("/topic/*", "/user/**").hasAnyRole("ADMIN", "USER")
                //.simpMessageDestMatchers("/topic/*").hasRole("ADMIN")
                //.simpSubscribeDestMatchers( "/user/**").hasAnyRole("ADMIN","USER")
                //.simpMessageDestMatchers( "/user/**").hasAnyRole("ADMIN","USER")
				.simpDestMatchers("/app/**").authenticated()
				.simpTypeMatchers(SUBSCRIBE, MESSAGE).denyAll()
				.anyMessage().denyAll();
	}

	@Override
	protected boolean sameOriginDisabled() {
		// While CSRF is disabled..
		return true;
	}
}
