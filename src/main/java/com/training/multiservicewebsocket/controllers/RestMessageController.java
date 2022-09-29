package com.training.multiservicewebsocket.controllers;

import com.training.multiservicewebsocket.services.WebSocketServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/")
public class RestMessageController {

    private final WebSocketServices webSocketServices;

    @PostMapping("/send-message/{toUser}")
    public void sendMessage(@RequestBody String message, @PathVariable String toUser) {
        log.info("Rest send message {} to user {}.", message, toUser);
        webSocketServices.notifyUser(toUser, message);
    }

    @PostMapping("/broadcast-message")
    public void sendMessage(@RequestBody String message) {
        log.info("Broadcastind message {} to everybody.", message);
        webSocketServices.notifyEverybody(message);
    }

}
