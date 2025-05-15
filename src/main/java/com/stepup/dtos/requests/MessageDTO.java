package com.stepup.dtos.requests;

import com.stepup.dtos.responses.UserRespone;
import com.stepup.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private UserRespone sender;
    private String content;
    private String messageType;
    private String fileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private boolean CSKH;
}
