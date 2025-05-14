package com.stepup.mapper;

import com.stepup.dtos.requests.ConversationDTO;
import com.stepup.dtos.requests.MessageDTO;
import com.stepup.entity.Conversation;
import com.stepup.entity.Message;
import com.stepup.service.impl.MessageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IUserMapper.class, IMessageMapper.class})
public abstract class ConversationMapper {

    @Autowired
    protected MessageService messageService;
    @Autowired
    protected IMessageMapper messageMapper;


    @Mapping(target = "unreadCount", expression = "java(countUnreadMessages(conversation))")
    @Mapping(target = "lastMessage", expression = "java(setLastMessage(conversation))")
    public abstract ConversationDTO toConversationDTO(Conversation conversation);

    protected Long countUnreadMessages(Conversation conversation) {
        return messageService.countUnreadMessages(conversation);
    }

    protected MessageDTO setLastMessage(Conversation conversation) {
        // Lấy tin nhắn gần nhất
        List<Message> messages = messageService.getMessagesByConversation(conversation);
        if (!messages.isEmpty()) {
            return messageMapper.toMessageDTO(messages.get(messages.size() - 1));
        }
        return null;
    }

    public abstract List<ConversationDTO> toConversationDTOList(List<Conversation> conversationList);
}

