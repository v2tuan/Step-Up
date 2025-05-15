package com.stepup.dtos.requests;

import com.stepup.dtos.responses.UserRespone;
import com.stepup.entity.Conversation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationDTO {
    private Long id;
    private UserRespone customer;
    private String title;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long unreadCount;  // Số tin nhắn chưa đọc
    private MessageDTO lastMessage;  // Tin nhắn gần nhất

    // Phương thức chuyển đổi từ Entity sang DTO
//    public static ConversationDTO fromEntity(Conversation conversation) {
//        return ConversationDTO.builder()
//                .id(conversation.getId())
//                .customer(UserDTO.fromEntity(conversation.getCustomer()))
//                .title(conversation.getTitle())
//                .status(conversation.getStatus().name())
//                .createdAt(conversation.getCreatedAt())
//                .updatedAt(conversation.getUpdatedAt())
//                .build();
//    }
}
