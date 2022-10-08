package com.training.multiservicewebsocket.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long id;

    @NotBlank(message = "Veuillez renseigner le message")
    private String text;

    private String to;

    private String username;

    private boolean isPrivate = false;
}
