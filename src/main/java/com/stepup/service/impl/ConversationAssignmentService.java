package com.stepup.service.impl;

import com.stepup.entity.Conversation;
import com.stepup.entity.ConversationAssignment;
import com.stepup.entity.User;
import com.stepup.repository.ConversationAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationAssignmentService {
    private final ConversationAssignmentRepository assignmentRepository;

    @Transactional(readOnly = true)
    public Optional<ConversationAssignment> getAssignmentByConversation(Conversation conversation) {
        return assignmentRepository.findByConversation(conversation);
    }

    @Transactional(readOnly = true)
    public List<ConversationAssignment> getAssignmentsByStaff(User staff) {
        return assignmentRepository.findByStaffOrderByAssignedAtDesc(staff);
    }

    @Transactional
    public ConversationAssignment assignConversation(Conversation conversation, User staff) {
        // Kiểm tra xem cuộc hội thoại đã được gán chưa
        Optional<ConversationAssignment> existingAssignment = getAssignmentByConversation(conversation);

        if (existingAssignment.isPresent()) {
            // Nếu đã được gán, cập nhật nhân viên mới
            ConversationAssignment assignment = existingAssignment.get();
            assignment.setStaff(staff);
            return assignmentRepository.save(assignment);
        } else {
            // Nếu chưa được gán, tạo mới
            ConversationAssignment assignment = ConversationAssignment.builder()
                    .conversation(conversation)
                    .staff(staff)
                    .build();

            return assignmentRepository.save(assignment);
        }
    }

    @Transactional
    public void removeAssignment(Conversation conversation) {
        Optional<ConversationAssignment> existingAssignment = getAssignmentByConversation(conversation);
        existingAssignment.ifPresent(assignmentRepository::delete);
    }
}
