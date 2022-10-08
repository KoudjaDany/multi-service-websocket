package com.training.multiservicewebsocket.repository;

import com.training.multiservicewebsocket.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findById(Long id);

    List<Message> findAll();

    List<Message> findAllByUsernameOrToOrToIsOrderByDate(String username, String recipient, String allRecipient);

    List<Message> findByText(String content);

    void delete(Message message);

    void deleteBy(String id);

}
