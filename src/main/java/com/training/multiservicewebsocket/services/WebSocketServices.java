package com.training.multiservicewebsocket.services;

import com.training.multiservicewebsocket.services.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketServices {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public void notifyUser(final String userId, final String message) {
        send(userId, message);
    }

    public void notifyEverybody(final String message) {
        send(message);
    }

    @SneakyThrows
    private void send(String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId, "/topic/messages", new MessageResponse(message));
    }

    @SneakyThrows
    private void send(String message) {
        messagingTemplate.convertAndSend("/topic/messages", new MessageResponse(message));
    }


    public void notifyEverybody(MessageResponse response) {
        messagingTemplate.convertAndSend("/topic/messages", response);
        notificationService.sendGlobalNotification(response);
    }

    public void notifyUser(MessageResponse response) {
        messagingTemplate.convertAndSendToUser(response.getTo(), "/topic/private-messages", response);
        messagingTemplate.convertAndSendToUser(response.getUsername(), "/topic/private-messages", response);
        notificationService.sendPrivateNotification(response);
    }
}
