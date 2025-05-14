package com.stepup.controller;

import com.stepup.components.SecurityUtils;
import com.stepup.entity.Conversation;
import com.stepup.entity.Message;
import com.stepup.entity.User;
import com.stepup.service.impl.ConversationService;
import com.stepup.service.impl.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/chat")
@RequiredArgsConstructor
public class AdminChatController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final SecurityUtils securityUtils;

    // Trang danh sách cuộc hội thoại
    @GetMapping
    public String chatDashboard(Model model) {
        List<Conversation> openConversations = conversationService.getConversationsByStatus(Conversation.ConversationStatus.OPEN);
        model.addAttribute("conversations", openConversations);
        return "admin/chat/dashboard";
    }

    // Trang chi tiết cuộc hội thoại
    @GetMapping("/{id}")
    public String viewConversation(@PathVariable Long id, Model model) {
        Optional<Conversation> conversationOpt = conversationService.getConversationById(id);

        if (conversationOpt.isEmpty()) {
            return "redirect:/admin/chat";
        }

        Conversation conversation = conversationOpt.get();
        List<Message> messages = messageService.getMessagesByConversation(conversation);

        // Đánh dấu tất cả tin nhắn là đã đọc
        User currentUser = securityUtils.getLoggedInUser();
        messageService.markAllAsRead(conversation, currentUser);

        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        model.addAttribute("currentUser", currentUser);

        return "admin/chat/conversation";
    }

    // Đóng cuộc hội thoại
    @PostMapping("/{id}/close")
    public String closeConversation(@PathVariable Long id) {
        Optional<Conversation> conversationOpt = conversationService.getConversationById(id);

        if (conversationOpt.isPresent()) {
            conversationService.closeConversation(conversationOpt.get());
        }

        return "redirect:/admin/chat";
    }
}
