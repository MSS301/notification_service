package com.notification_svc.notification.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.notification_svc.notification.service.EmailService;

@Service
public class LoggingEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailService.class);

    @Override
    public void sendEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            log.warn("Skip sending email because recipient address is missing. subject={}", subject);
            return;
        }
        log.info("Sending notification email to {} with subject '{}'. Body:\n{}", to, subject, body);
    }
}
