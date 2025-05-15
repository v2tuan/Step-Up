package com.stepup.controller;

import com.stepup.Enum.Role;
import com.stepup.components.SecurityUtils;
import com.stepup.dtos.requests.ChatMessageRequest;
import com.stepup.dtos.requests.MessageDTO;
import com.stepup.entity.Conversation;
import com.stepup.entity.Message;
import com.stepup.entity.User;
import com.stepup.mapper.IMessageMapper;
import com.stepup.mapper.IUserMapper;
import com.stepup.service.impl.ConversationService;
import com.stepup.service.impl.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final SecurityUtils securityUtils;
    private final SimpMessagingTemplate messagingTemplate;
    private final IMessageMapper messageMapper;
    private final IUserMapper userMapper;

    // Xử lý tin nhắn từ client thông qua WebSocket
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        // Gửi tin nhắn đến tất cả client đang lắng nghe topic này
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();;
        boolean cskh = currentUser.getRole().equals(Role.CUSTOMER) ? false : true;
        MessageDTO messageDto = MessageDTO.builder()
                .createdAt(LocalDateTime.now())
                .conversationId(request.getConversationId())
                .content(request.getContent())
                .messageType(request.getMessageType())
                .sender(userMapper.toUserRespone(currentUser))
                .fileUrl(request.getFileUrl())
                .CSKH(cskh)
                .build();
        messagingTemplate.convertAndSend("/topic/conversation." + request.getConversationId(), messageDto); // giành cho người dùng
        messagingTemplate.convertAndSend("/topic/conversation", messageDto); // giành cho admin
        Optional<Conversation> conversationOpt = conversationService.getConversationById(request.getConversationId());

        if (conversationOpt.isPresent()) {
            Conversation conversation = conversationOpt.get();
//            User currentUser = securityUtils.getLoggedInUser();
//            User currentUser = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();;

            Message.MessageType messageType = Message.MessageType.valueOf(request.getMessageType());

            boolean isSystem = currentUser.getRole().equals(Role.CUSTOMER) ? false : true;

            // Tạo tin nhắn mới
            Message message = messageService.createMessage(
                    conversation,
                    currentUser,
                    request.getContent(),
                    messageType,
                    request.getFileUrl(),
                    isSystem
            );

            MessageDTO messageDTO = messageMapper.toMessageDTO(message);

            // Gửi tin nhắn đến tất cả client đang lắng nghe topic này
//            messagingTemplate.convertAndSend("/topic/conversation." + conversation.getId(), messageDTO);
        }
    }

    // Xử lý sự kiện đánh dấu đã đọc
    @MessageMapping("/chat.markRead")
    public void markAsRead(@Payload Long messageId) {
        Optional<Message> messageOpt = messageService.getMessageById(messageId);

        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            messageService.markAsRead(message);

            // Thông báo cho tất cả client biết tin nhắn đã được đọc
            messagingTemplate.convertAndSend(
                    "/topic/conversation." + message.getConversation().getId() + ".read",
                    messageMapper.toMessageDTO(message)
            );

            // Thông báo cho admin biết tin nhắn đã được đọc
            messagingTemplate.convertAndSend(
                    "/topic/conversation" + ".read",
                    messageMapper.toMessageDTO(message)
            );
        }
    }
}
