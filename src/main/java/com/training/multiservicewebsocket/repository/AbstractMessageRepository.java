package com.training.multiservicewebsocket.repository;

import com.training.multiservicewebsocket.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbstractMessageRepository {

    Message save(Message message);

    Optional<Message> findById(String id);

    List<Message> findAll();

    List<Message> findByText(String content);

    void delete(Message message);

    void deleteBy(String id);

}
