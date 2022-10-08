package com.training.multiservicewebsocket.repository;

import com.training.multiservicewebsocket.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class MessageRepositoryImpl implements AbstractMessageRepository {

    private static final Set<Message> MESSAGES = new HashSet<>();

    @Override
    public Message save(Message message) {
        if (message.isNew()) {
            message.setId(new Random().nextLong());
        }
        MESSAGES.add(message);
        return message;
    }

    @Override
    public Optional<Message> findById(String id) {
        return MESSAGES.stream().filter(message -> message.getId().equals(id)).findFirst();
    }

    @Override
    public List<Message> findAll() {
        return new LinkedList<>(MESSAGES);
    }

    @Override
    public List<Message> findByText(String text) {
        return MESSAGES.stream().filter(message -> message.getText().contains(text)).collect(Collectors.toList());
    }

    @Override
    public void delete(Message message) {
        MESSAGES.remove(message);
    }

    @Override
    public void deleteBy(String id) {
        MESSAGES.removeIf(message -> message.getId().equals(id));
    }
}
