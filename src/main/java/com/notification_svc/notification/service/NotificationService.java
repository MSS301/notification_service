package com.notification_svc.notification.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.notification_svc.notification.dto.NotificationRequest;
import com.notification_svc.notification.model.Notification;

public interface NotificationService {

    Notification create(NotificationRequest request);

    Notification getById(Integer id);

    List<Notification> getAll();

    List<Notification> getByUserId(UUID userId);

    Page<Notification> getByUserId(UUID userId, Pageable pageable);

    List<Notification> getByType(String type);

    Notification update(Integer id, NotificationRequest request);

    void delete(Integer id);

    void markAsRead(Integer id);

    void sendEmail(Notification notification);

    Notification notifyUser(
            UUID userId, String recipientEmail, String title, String message, String type, String event, String link);

    default Notification notifyUser(
            UUID userId, String recipientEmail, String title, String message, String type, String event) {
        return notifyUser(userId, recipientEmail, title, message, type, event, null);
    }
}
