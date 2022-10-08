package com.training.multiservicewebsocket.services;


import com.training.multiservicewebsocket.domain.Message;
import com.training.multiservicewebsocket.services.dto.MessageDto;
import com.training.multiservicewebsocket.services.dto.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageResponse save(MessageDto messageDto);

    MessageResponse findById(Long id);

    List<MessageResponse> findAll();

    List<MessageResponse> findAllForCurrentUser();

    List<MessageResponse> findByContent(String content);

    void delete(Message message);

    void deleteBy(String id);

}
