package com.training.multiservicewebsocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Configuration
public class WebSocketHandlerConfig {

    @Bean
    public WebSocketConnectHandler webSocketConnectHandler(SimpMessageSendingOperations messageSendingOperations) {
        return new WebSocketConnectHandler(messageSendingOperations);
    }

    @Bean
    public WebSocketDisconnectHandler webSocketDisconnectHandler(SimpMessageSendingOperations messageSendingOperations) {
        return new WebSocketDisconnectHandler(messageSendingOperations);
    }

}
