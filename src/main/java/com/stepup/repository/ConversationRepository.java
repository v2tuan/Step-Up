package com.stepup.repository;

import com.stepup.entity.Conversation;
import com.stepup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // Tìm tất cả cuộc hội thoại của một khách hàng
    List<Conversation> findByCustomerOrderByUpdatedAtDesc(User customer);

    // Tìm tất cả cuộc hội thoại theo trạng thái
    List<Conversation> findByStatusOrderByUpdatedAtDesc(Conversation.ConversationStatus status);
}
