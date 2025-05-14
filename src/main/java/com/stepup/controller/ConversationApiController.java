package com.stepup.controller;

import com.stepup.components.SecurityUtils;
import com.stepup.dtos.requests.ConversationDTO;
import com.stepup.entity.Conversation;
import com.stepup.entity.Message;
import com.stepup.entity.User;
import com.stepup.mapper.ConversationMapper;
import com.stepup.mapper.IMessageMapper;
import com.stepup.service.impl.ConversationService;
import com.stepup.service.impl.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat/conversations")
@RequiredArgsConstructor
public class ConversationApiController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final SecurityUtils securityUtils;
    private final ConversationMapper conversationMapper;
    private final IMessageMapper messageMapper;

    // Lấy tất cả cuộc hội thoại
    @GetMapping("/all")
    public ResponseEntity<?> getConversations() {
        List<Conversation> conversations = conversationService.getConversationsByStatus(Conversation.ConversationStatus.OPEN);
        return ResponseEntity.ok(conversationMapper.toConversationDTOList(conversations));
    }

    // Lấy tất cả cuộc hội thoại của người dùng hiện tại
    @GetMapping
    public ResponseEntity<ConversationDTO> getMyConversations() {
        // Lấy thông tin người dùng hiện tại
        User currentUser = securityUtils.getLoggedInUser();

        // Lấy danh sách cuộc hội thoại
        List<Conversation> conversations = conversationService.getConversationsByCustomer(currentUser);

        if(!conversations.isEmpty()) {
            // Chuyển đổi sang DTO và thêm thông tin bổ sung
            List<ConversationDTO> conversationDTOs = conversations.stream()
                    .map(conversation -> {
                        ConversationDTO dto = conversationMapper.toConversationDTO(conversation);

                        // Lấy số tin nhắn chưa đọc
                        dto.setUnreadCount(messageService.countUnreadMessages(conversation));

                        // Lấy tin nhắn gần nhất
                        List<Message> messages = messageService.getMessagesByConversation(conversation);
                        if (!messages.isEmpty()) {
                            dto.setLastMessage(messageMapper.toMessageDTO(messages.get(messages.size() - 1)));
                        }

                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(conversationDTOs.getFirst());
        }else {
            Conversation conversation = conversationService.createConversation(currentUser, "");
            ConversationDTO conversationDTO = conversationMapper.toConversationDTO(conversation);
            // Tạo tin nhắn mới
            String content = "Xin chào! Cảm ơn bạn đã liên hệ với chúng tôi. Chúng tôi sẽ hỗ trợ bạn trong thời gian sớm nhất.";

            boolean isSystem = true;
            Message message = messageService.createMessage(
                    conversation,
                    null,
                    content,
                    Message.MessageType.TEXT,
                    "",
                    isSystem
            );
            return ResponseEntity.ok(conversationDTO);
        }
    }

    // Lấy thông tin chi tiết của một cuộc hội thoại
    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversation(@PathVariable Long id) {
        Optional<Conversation> conversationOpt = conversationService.getConversationById(id);

        if (conversationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Conversation conversation = conversationOpt.get();
        ConversationDTO dto = conversationMapper.toConversationDTO(conversation);

        // Lấy số tin nhắn chưa đọc
        dto.setUnreadCount(messageService.countUnreadMessages(conversation));

        // Lấy tin nhắn gần nhất
        List<Message> messages = messageService.getMessagesByConversation(conversation);
        if (!messages.isEmpty()) {
            dto.setLastMessage(messageMapper.toMessageDTO(messages.get(messages.size() - 1)));
        }

        return ResponseEntity.ok(dto);
    }

    // Tạo cuộc hội thoại mới
    @PostMapping
    public ResponseEntity<ConversationDTO> createConversation() {
        // Lấy thông tin người dùng hiện tại
        User currentUser = securityUtils.getLoggedInUser();

        // Tạo cuộc hội thoại mới
        Conversation conversation = conversationService.createConversation(currentUser, "");

        return ResponseEntity.ok(conversationMapper.toConversationDTO(conversation));
    }

    // Đóng cuộc hội thoại
    @PutMapping("/{id}/close")
    public ResponseEntity<ConversationDTO> closeConversation(@PathVariable Long id) {
        Optional<Conversation> conversationOpt = conversationService.getConversationById(id);

        if (conversationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Conversation conversation = conversationOpt.get();
        conversationService.closeConversation(conversation);

        return ResponseEntity.ok(conversationMapper.toConversationDTO(conversation));
    }
}
