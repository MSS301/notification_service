package com.notification_svc.notification.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
