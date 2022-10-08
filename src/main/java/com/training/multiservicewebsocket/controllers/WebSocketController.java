package com.training.multiservicewebsocket.controllers;

import com.training.multiservicewebsocket.services.MessageService;
import com.training.multiservicewebsocket.services.NotificationService;
import com.training.multiservicewebsocket.services.WebSocketServices;
import com.training.multiservicewebsocket.services.dto.MessageDto;
import com.training.multiservicewebsocket.services.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {


    private final MessageService messageService;
    private final WebSocketServices webSocketServices;
    private final NotificationService notificationService;

    // @MessageMapping("/message")
    //@SendTo("/topic/messages")
    public Boolean sendMessage(@RequestBody MessageDto message) {
        log.info("BroadCasting message '{}' to Everybody", message.getText());
        webSocketServices.notifyEverybody(message.getText());
        return Boolean.TRUE;
    }

    //@MessageMapping("/message/{toUser}")
    //@SendToUser("/topic/messages")
    public Boolean sendMessage(Principal principal,
                               @Header String authKey,
                               @DestinationVariable String toUser,
                               @RequestBody MessageDto message) {
        log.info("Sending the message '{}' from User '{}' to '{}'. Auth key {}", message.getText(), principal.getName(), toUser, authKey);
        webSocketServices.notifyUser(toUser, message.getText());
        return Boolean.TRUE;
    }


    @MessageMapping("/message")
    //@SendTo("/topic/messages")
    public void sendMessage(@Valid MessageDto messageDto, final Principal principal) throws InterruptedException {
        log.info("BroadCasting message '{}' to Everybody", messageDto.getText());
        messageDto.setUsername(principal.getName());
        final MessageResponse messageResponse = messageService.save(messageDto);
        Thread.sleep(1000);
        webSocketServices.notifyEverybody(messageResponse);
    }

    @MessageMapping("/private-message")
    //@SendToUser("/topic/private-messages")
    public void sendPrivateMessage(@Valid MessageDto messageDto, final Principal principal) throws InterruptedException {
        log.info("Sending the message '{}' from User '{}' to '{}'", messageDto.getText(), principal.getName(), messageDto.getTo());
        messageDto.setPrivate(true);
        messageDto.setUsername(principal.getName());
        final MessageResponse messageResponse = messageService.save(messageDto);
        webSocketServices.notifyUser(messageResponse);
        Thread.sleep(1000);
    }


    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        final String message = exception.getMessage();
        log.error(message, exception);
        return message;
    }

}
