package com.notification_svc.notification.service.impl;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.notification_svc.notification.dto.NotificationRequest;
import com.notification_svc.notification.model.Notification;
import com.notification_svc.notification.repository.NotificationRepository;
import com.notification_svc.notification.service.EmailService;
import com.notification_svc.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public Notification create(NotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .recipientEmail(request.getRecipientEmail())
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .event(request.getEvent())
                .link(request.getLink())
                .build();
        Notification saved = notificationRepository.save(notification);
        handleSideEffects(saved);
        return saved;
    }

    @Override
    public Notification getById(Integer id) {
        return notificationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Notification not found with id " + id));
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> getByUserId(UUID userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public Page<Notification> getByUserId(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<Notification> getByType(String type) {
        return notificationRepository.findByType(type);
    }

    @Override
    @Transactional
    public Notification update(Integer id, NotificationRequest request) {
        Notification existing = getById(id);
        existing.setUserId(request.getUserId());
        existing.setRecipientEmail(request.getRecipientEmail());
        existing.setTitle(request.getTitle());
        existing.setMessage(request.getMessage());
        existing.setType(request.getType());
        existing.setEvent(request.getEvent());
        existing.setLink(request.getLink());
        Notification saved = notificationRepository.save(existing);
        return saved;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Notification not found with id " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void markAsRead(Integer id) {
        Notification notification = getById(id);
        if (Boolean.TRUE.equals(notification.getSeen())) {
            return;
        }
        notification.setSeen(Boolean.TRUE);
        notification.setSeenAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public void sendEmail(Notification notification) {
        if (notification.getRecipientEmail() == null
                || notification.getRecipientEmail().isBlank()) {
            return;
        }
        String subject = "[Notification] " + notification.getTitle();
        StringBuilder body = new StringBuilder();
        if (notification.getMessage() != null && !notification.getMessage().isBlank()) {
            body.append(notification.getMessage());
        } else {
            body.append(notification.getTitle());
        }
        if (notification.getLink() != null && !notification.getLink().isBlank()) {
            body.append("\n\nView details: ").append(notification.getLink());
        }
        emailService.sendEmail(notification.getRecipientEmail(), subject, body.toString());
    }

    @Override
    @Transactional
    public Notification notifyUser(
            UUID userId, String recipientEmail, String title, String message, String type, String event, String link) {
        Notification notification = Notification.builder()
                .userId(userId)
                .recipientEmail(recipientEmail)
                .title(title)
                .message(message)
                .type(type)
                .event(event)
                .link(link)
                .build();
        Notification saved = notificationRepository.save(notification);
        handleSideEffects(saved);
        return saved;
    }

    private void handleSideEffects(Notification notification) {
        sendEmail(notification);
        if (notification.getUserId() != null) {
            String destination = "/topic/notification/user/" + notification.getUserId();
            messagingTemplate.convertAndSend(destination, notification);
        }
    }
}
