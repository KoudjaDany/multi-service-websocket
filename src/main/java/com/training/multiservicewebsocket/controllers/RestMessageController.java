package com.training.multiservicewebsocket.controllers;

import com.training.multiservicewebsocket.services.MessageService;
import com.training.multiservicewebsocket.services.WebSocketServices;
import com.training.multiservicewebsocket.services.dto.MessageDto;
import com.training.multiservicewebsocket.services.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/messages")
public class RestMessageController {

    private final MessageService messageService;
    private final WebSocketServices webSocketService;

    @PostMapping("/send-message/{toUser}")
    public void sendMessage(@RequestBody String message, @PathVariable String toUser) {
        log.info("Rest send message {} to user {}.", message, toUser);

        webSocketService.notifyUser(toUser, message);
    }

    @PostMapping("/broadcast-message")
    public void sendMessage(@RequestBody String message) {
        log.info("Broadcasting message {} to everybody.", message);
        webSocketService.notifyEverybody(message);
    }


    @GetMapping(value = "/all-messages")
    public ResponseEntity<List<MessageResponse>> allMessages() {
        return ResponseEntity.ok(messageService.findAll());
    }

    @GetMapping(value = "/all-my-messages")
    public ResponseEntity<List<MessageResponse>> allMyMessages() {
        return ResponseEntity.ok(messageService.findAllForCurrentUser());
    }

    @PostMapping(value = "/send-message")
    public void sendMessage(@RequestBody @Valid MessageDto messageDto) {
        final MessageResponse messageResponse = messageService.save(messageDto);
        webSocketService.notifyEverybody(messageResponse);
    }

    @PostMapping(value = "/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable String id, @RequestBody @Valid MessageDto messageDto) {
        messageDto.setTo(id);
        messageDto.setPrivate(true);
        final MessageResponse messageResponse = messageService.save(messageDto);
        webSocketService.notifyUser(messageResponse);
    }

}
