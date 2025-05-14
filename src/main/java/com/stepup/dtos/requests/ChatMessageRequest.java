package com.stepup.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    private Long conversationId;
    private String content;
    private String messageType;  // TEXT, IMAGE, FILE
    private LocalDateTime createdAt;
    private String fileUrl;
}