package com.stepup.service.impl;

import com.stepup.entity.Conversation;
import com.stepup.entity.Message;
import com.stepup.entity.User;
import com.stepup.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    @Transactional(readOnly = true)
    public List<Message> getMessagesByConversation(Conversation conversation) {
        return messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
    }

    @Transactional(readOnly = true)
    public Page<Message> getMessagesByConversation(Conversation conversation, Pageable pageable) {
        return messageRepository.findByConversation(conversation, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    @Transactional
    public Message createMessage(Conversation conversation, User sender, String content, Message.MessageType messageType, String fileUrl, boolean isSystem) {
        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(content)
                .messageType(messageType)
                .fileUrl(fileUrl)
                .CSKH(isSystem)
                .build();

        // Cập nhật thời gian của cuộc hội thoại
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationService.updateConversation(conversation);

        return messageRepository.save(message);
    }

    @Transactional
    public void markAsRead(Message message) {
        message.setReadAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Transactional
    public void markAllAsRead(Conversation conversation, User reader) {
        List<Message> unreadMessages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation)
                .stream()
                .filter(m -> m.getReadAt() == null && m.getSender() != null && !m.getSender().equals(reader))
                .toList();

        for (Message message : unreadMessages) {
            message.setReadAt(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    @Transactional(readOnly = true)
    public Long countUnreadMessages(Conversation conversation) {
        return messageRepository.countByConversationAndReadAtIsNull(conversation);
    }
}

