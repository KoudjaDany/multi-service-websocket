package com.training.multiservicewebsocket.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private String username;
    private boolean isPrivate;
    private String text;
    private String to;
    @DateTimeFormat(pattern = "dd/MM/yyy hh:mm")
    private Date date;

    public MessageResponse(String message) {
        this.text = message;
    }
    public MessageResponse(String username, String message) {
        this.username = username;
        this.text = message;
    }
}
