package com.stepup.repository;

import com.stepup.entity.Conversation;
import com.stepup.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Tìm tất cả tin nhắn của một cuộc hội thoại, sắp xếp theo thời gian tạo
    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);

    // Tìm tất cả tin nhắn của một cuộc hội thoại với phân trang
    Page<Message> findByConversation(Conversation conversation, Pageable pageable);

    // Đếm số tin nhắn chưa đọc trong một cuộc hội thoại
    Long countByConversationAndReadAtIsNull(Conversation conversation);
}
