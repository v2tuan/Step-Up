package com.stepup.mapper;

import com.stepup.dtos.requests.MessageDTO;
import com.stepup.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = IUserMapper.class)
public interface IMessageMapper {
    @Mapping(target = "conversationId", expression = "java(map(message))")
    MessageDTO toMessageDTO(Message message);

    default long map(Message message){
        return message.getConversation().getId();
    }
}
