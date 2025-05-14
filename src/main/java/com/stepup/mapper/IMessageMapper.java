package com.stepup.mapper;

import com.stepup.dtos.requests.MessageDTO;
import com.stepup.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = IUserMapper.class)
public interface IMessageMapper {
    MessageDTO toMessageDTO(Message message);
}
