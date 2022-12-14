package com.training.multiservicewebsocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {


    @Autowired
    private WebSocketChannelInterceptor channelInterceptor;

    @Value("${broker.host}")
    private String brokerHost;

    @Value("${broker.port}")
    private int brokerPort;

    @Value("${broker.username}")
    private String brokerUser;

    @Value("${broker.password}")
    private String brokerPassword;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
       //registry.enableStompBrokerRelay("/queue", "/topic")
       //        .setRelayHost(brokerHost)
       //        .setRelayPort(brokerPort)
       //        .setClientLogin(brokerUser)
       //        .setClientPasscode(brokerPassword)
       //        .setSystemLogin(brokerUser)
       //        .setSystemPasscode(brokerPassword)
       //        .setUserDestinationBroadcast("/topic/unresolved-user")
       //        .setUserRegistryBroadcast("/topic/log-user-registry")
       //;

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-register")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }
}
