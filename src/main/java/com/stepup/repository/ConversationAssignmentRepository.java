package com.stepup.repository;

import com.stepup.entity.Conversation;
import com.stepup.entity.ConversationAssignment;
import com.stepup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationAssignmentRepository extends JpaRepository<ConversationAssignment, Long> {
    // Tìm phân công cho một cuộc hội thoại
    Optional<ConversationAssignment> findByConversation(Conversation conversation);

    // Tìm tất cả cuộc hội thoại được gán cho một nhân viên CSKH
    List<ConversationAssignment> findByStaffOrderByAssignedAtDesc(User staff);
}
