package com.hiringwire.model;

import com.hiringwire.model.enums.MessageType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Long senderId;
    private Long recipientId;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private MessageType type;
}