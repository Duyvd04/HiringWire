package com.hiringwire.model.request;

import com.hiringwire.model.enums.NotificationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    private Long userId;
    private String message;
    private String action;
    private String route;
    private NotificationStatus status;
    private LocalDateTime timestamp;
}
