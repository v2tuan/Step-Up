package com.stepup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//-- Bảng lưu trữ phân công nhân viên CSKH cho cuộc hội thoại
//-- (Chuẩn bị cho tương lai khi cần phân chia nhân viên)
public class ConversationAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    // Pre-persist hook
    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}