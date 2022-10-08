package com.training.multiservicewebsocket.services;

import com.training.multiservicewebsocket.domain.Message;
import com.training.multiservicewebsocket.repository.MessageRepository;
import com.training.multiservicewebsocket.services.dto.MessageDto;
import com.training.multiservicewebsocket.services.dto.MessageResponse;
import com.training.multiservicewebsocket.services.mappers.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageResponse save(MessageDto messageDto) {
        final Message message = messageMapper.toEntity(messageDto);
        message.setDate(ZonedDateTime.now());
        return messageMapper.toResponse(messageRepository.save(message));
    }

    @Override
    public MessageResponse findById(Long id) {
        return messageRepository.findById(id)
                .map(messageMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Le message n'existe pas"));
    }

    @Override
    public List<MessageResponse> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> findAllForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return messageRepository.findAllByUsernameOrToOrToIsOrderByDate(username, username, "")
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> findByContent(String content) {
        return messageRepository.findByText(content)
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public void deleteBy(String id) {
        messageRepository.deleteBy(id);
    }
}
