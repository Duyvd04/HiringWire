package com.hiringwire.api;

import com.hiringwire.model.ChatMessage;
import com.hiringwire.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("Received Message: " + chatMessage); // Debug log
        chatService.saveMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getSenderId() + "/" + chatMessage.getRecipientId(), chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getRecipientId() + "/" + chatMessage.getSenderId(), chatMessage);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat/history/{userId}/{recipientId}")
    public List<ChatMessage> getChatHistory(@PathVariable Long userId, @PathVariable Long recipientId) {
        return chatService.getChatHistory(userId, recipientId);
    }
}