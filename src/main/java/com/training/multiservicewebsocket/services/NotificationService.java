package com.training.multiservicewebsocket.services;

import com.training.multiservicewebsocket.services.dto.MessageResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification(MessageResponse messageResponse){
        messagingTemplate.convertAndSend("/topic/global-notifications", messageResponse);
    }

    public void sendPrivateNotification(MessageResponse messageResponse){
        messagingTemplate.convertAndSendToUser(messageResponse.getTo(), "/topic/private-notifications", messageResponse);
    }
}
