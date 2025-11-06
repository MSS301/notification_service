package com.notification_svc.notification.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification_svc.notification.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserId(UUID userId);

    List<Notification> findByUserIdAndSeen(UUID userId, Boolean seen);

    List<Notification> findByType(String type);

    Page<Notification> findByUserId(UUID userId, Pageable pageable);
}
