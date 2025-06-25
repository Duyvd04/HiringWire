package com.hiringwire.service;

import com.hiringwire.model.ChatMessage;
import com.hiringwire.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        System.out.println("Saving Message: " + message); // Debug log
        message.setTimestamp(LocalDateTime.now());
        ChatMessage savedMessage = chatMessageRepository.save(message);
        System.out.println("Saved Message: " + savedMessage);
        return savedMessage;
    }

    public List<ChatMessage> getChatHistory(Long userId, Long recipientId) {
        return chatMessageRepository.findChatHistoryBetweenUsers(userId, recipientId);
    }
}