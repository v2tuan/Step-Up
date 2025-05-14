package com.stepup.controller;

import com.stepup.components.SecurityUtils;
import com.stepup.dtos.requests.ChatMessageRequest;
import com.stepup.dtos.requests.MessageDTO;
import com.stepup.entity.Conversation;
import com.stepup.entity.Message;
import com.stepup.entity.User;
import com.stepup.mapper.IMessageMapper;
import com.stepup.service.impl.ConversationService;
import com.stepup.service.impl.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat/messages")
@RequiredArgsConstructor
public class MessageApiController {

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final SecurityUtils securityUtils;
    private final SimpMessagingTemplate messagingTemplate;
    private final IMessageMapper messageMapper;

    // Lấy tin nhắn của một cuộc hội thoại
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages(
            @RequestParam Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Optional<Conversation> conversationOpt = conversationService.getConversationById(conversationId);

        if (conversationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Conversation conversation = conversationOpt.get();
//        Pageable pageable = PageRequest.of(page, size);
        // Sắp xếp theo thời gian tạo (mới nhất ở cuối cùng)
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        // Nếu bạn muốn tin nhắn mới nhất ở đầu thì dùng descending()
//         Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Message> messagePage = messageService.getMessagesByConversation(conversation, pageable);

        // Đánh dấu tin nhắn đã đọc
        User currentUser = securityUtils.getLoggedInUser();
        messageService.markAllAsRead(conversation, currentUser);

        List<MessageDTO> messageDTOs = messagePage.getContent().stream()
                .map(messageMapper::toMessageDTO)
                .collect(Collectors.toList());

        // Sau đó sắp xếp lại theo tăng dần createdAt (nếu muốn)
        messageDTOs.sort(Comparator.comparing(MessageDTO::getCreatedAt)); // ASC

        return ResponseEntity.ok(messageDTOs);
    }

//     Gửi tin nhắn mới
    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody ChatMessageRequest request) {
        Optional<Conversation> conversationOpt = conversationService.getConversationById(request.getConversationId());

        if (conversationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Conversation conversation = conversationOpt.get();
        User currentUser = securityUtils.getLoggedInUser();

        Message.MessageType messageType = Message.MessageType.valueOf(request.getMessageType());

        // Tạo tin nhắn mới
        Message message = messageService.createMessage(
                conversation,
                currentUser,
                request.getContent(),
                messageType,
                request.getFileUrl()
        );

        MessageDTO messageDTO = messageMapper.toMessageDTO(message);

        // Gửi tin nhắn qua WebSocket
        messagingTemplate.convertAndSend("/topic/conversation." + conversation.getId(), messageDTO);

        return ResponseEntity.ok(messageDTO);
    }

    // Đánh dấu tin nhắn đã đọc
    @PutMapping("/{id}/read")
    public ResponseEntity<MessageDTO> markAsRead(@PathVariable Long id) {
        Optional<Message> messageOpt = messageService.getMessageById(id);

        if (messageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message message = messageOpt.get();
        messageService.markAsRead(message);

        return ResponseEntity.ok(messageMapper.toMessageDTO(message));
    }
}
