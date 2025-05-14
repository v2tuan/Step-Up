package com.stepup.service.impl;

import com.stepup.entity.Conversation;
import com.stepup.entity.User;
import com.stepup.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;

    @Transactional(readOnly = true)
    public List<Conversation> getConversationsByCustomer(User customer) {
        return conversationRepository.findByCustomerOrderByUpdatedAtDesc(customer);
    }

    @Transactional(readOnly = true)
    public List<Conversation> getConversationsByStatus(Conversation.ConversationStatus status) {
        return conversationRepository.findByStatusOrderByUpdatedAtDesc(status);
    }

    @Transactional(readOnly = true)
    public Optional<Conversation> getConversationById(Long id) {
        return conversationRepository.findById(id);
    }

    @Transactional
    public Conversation createConversation(User customer, String title) {
        Conversation conversation = Conversation.builder()
                .customer(customer)
                .title(title)
                .status(Conversation.ConversationStatus.OPEN)
                .build();

        return conversationRepository.save(conversation);
    }

    @Transactional
    public Conversation updateConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Transactional
    public void closeConversation(Conversation conversation) {
        conversation.setStatus(Conversation.ConversationStatus.CLOSED);
        conversationRepository.save(conversation);
    }
}
