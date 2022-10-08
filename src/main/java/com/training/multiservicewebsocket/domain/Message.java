package com.training.multiservicewebsocket.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String username;
    @Column(name = "recipient")
    private String to;
    private boolean isPrivate;
    private ZonedDateTime date;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Message && ((Message) obj).id.equals(id);
    }

    public boolean isNew() {
        return id == null;
    }
}
