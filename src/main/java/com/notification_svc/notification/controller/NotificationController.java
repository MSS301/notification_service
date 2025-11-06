package com.notification_svc.notification.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.notification_svc.notification.dto.NotificationRequest;
import com.notification_svc.notification.model.Notification;
import com.notification_svc.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Manage user notifications and delivery state")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "List notifications", description = "Retrieve all notifications or filter by type.")
    @GetMapping
    public List<Notification> getAllNotifications(@RequestParam(value = "type", required = false) String type) {
        if (type != null && !type.isBlank()) {
            return notificationService.getByType(type);
        }
        return notificationService.getAll();
    }

    @Operation(summary = "Get notification details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notification found"),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Integer id) {
        return notificationService.getById(id);
    }

    @Operation(summary = "Create a notification")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification createNotification(@Valid @RequestBody NotificationRequest request) {
        return notificationService.create(request);
    }

    @Operation(summary = "Update an existing notification")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notification updated"),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PutMapping("/{id}")
    public Notification updateNotification(@PathVariable Integer id, @Valid @RequestBody NotificationRequest request) {
        return notificationService.update(id, request);
    }

    @Operation(summary = "Delete a notification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Integer id) {
        notificationService.delete(id);
    }

    @Operation(summary = "Mark notification as read")
    @PostMapping("/{id}/mark-as-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
    }

    @Operation(summary = "Get notifications for a user")
    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUser(@PathVariable UUID userId) {
        return notificationService.getByUserId(userId);
    }

    @Operation(summary = "Get notifications for a user (paged)")
    @GetMapping("/user/{userId}/paged")
    public Page<Notification> getNotificationsByUserPaged(
            @PathVariable UUID userId,
            @Parameter(description = "Page configuration") @PageableDefault(size = 10) Pageable pageable) {
        return notificationService.getByUserId(userId, pageable);
    }

    @Operation(summary = "Resend notification email")
    @PostMapping("/{id}/send-email")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resendEmail(@PathVariable Integer id) {
        Notification notification = notificationService.getById(id);
        notificationService.sendEmail(notification);
    }
}
