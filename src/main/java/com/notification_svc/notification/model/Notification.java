package com.notification_svc.notification.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "userId is required")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Email
    @Column(name = "recipient_email")
    private String recipientEmail;

    @NotBlank(message = "title is required")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @NotBlank(message = "type is required")
    @Column(nullable = false)
    private String type;

    private String event;

    private String link;

    @Builder.Default
    @Column(nullable = false)
    private Boolean seen = Boolean.FALSE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "seen_at")
    private LocalDateTime seenAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        if (seen == null) {
            seen = Boolean.FALSE;
        }
    }
}
