package com.training.multiservicewebsocket.controllers;

import com.training.multiservicewebsocket.dto.MessageDto;
import com.training.multiservicewebsocket.services.WebSocketServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {


    private final WebSocketServices webSocketServices;

    @MessageMapping("/message")
        //@SendTo("/topic/messages")
    public Boolean sendMessage(@RequestBody MessageDto message) {
        log.info("BroadCasting message '{}' to Everybody", message.getMessageContent());
        webSocketServices.notifyEverybody(message.getMessageContent());
        return Boolean.TRUE;
    }

    @MessageMapping("/message/{toUser}")
    //@SendToUser("/topic/messages")
    public Boolean sendMessage(Principal principal,
                                       @Header String authKey,
                                       @DestinationVariable String toUser,
                                       @RequestBody MessageDto message) {
        log.info("Sending the message '{}' from User '{}' to '{}'. Auth key {}", message.getMessageContent(), principal.getName(), toUser, authKey);
        webSocketServices.notifyUser(toUser, message.getMessageContent());
        return Boolean.TRUE;
    }
}
