package com.notification_svc.notification.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(message = "userId is required")
    private UUID userId;

    @Email
    private String recipientEmail;

    @NotBlank(message = "title is required")
    private String title;

    private String message;

    @NotBlank(message = "type is required")
    private String type;

    private String event;

    private String link;
}
