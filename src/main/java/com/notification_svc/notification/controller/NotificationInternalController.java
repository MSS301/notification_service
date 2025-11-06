package com.notification_svc.notification.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.notification_svc.notification.dto.NotificationRequest;
import com.notification_svc.notification.model.Notification;
import com.notification_svc.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Internal API")
public class NotificationInternalController {

    private final NotificationService notificationService;

    @Operation(summary = "Notify user internally", description = "Used by other services to create notifications.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification notifyUser(@Valid @RequestBody NotificationRequest request) {
        return notificationService.notifyUser(
                request.getUserId(),
                request.getRecipientEmail(),
                request.getTitle(),
                request.getMessage(),
                request.getType(),
                request.getEvent(),
                request.getLink());
    }
}
