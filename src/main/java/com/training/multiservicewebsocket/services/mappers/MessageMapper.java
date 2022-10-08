package com.training.multiservicewebsocket.services.mappers;

import com.training.multiservicewebsocket.domain.Message;
import com.training.multiservicewebsocket.services.dto.MessageDto;
import com.training.multiservicewebsocket.services.dto.MessageResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {

    public abstract Message toEntity(MessageDto messageDto);

    public abstract MessageResponse toResponse(Message message);

}
