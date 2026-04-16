package com.coinwise.observer;

import com.coinwise.enums.NotificationType;
import com.coinwise.model.Notification;
import com.coinwise.model.User;
import com.coinwise.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PushNotification implements BudgetObserver {

    private final NotificationRepository notificationRepository;

    @Autowired
    public PushNotification(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void update(User user, String msg) {
        // Simulating Push Notification Logic
        System.out.println("SENDING PUSH NOTIF to " + user.getUsername() + ": " + msg);
        
        // Save Notification to DB
        Notification notif = Notification.builder()
                .user(user)
                .message("[PUSH ALERT] " + msg)
                .type(NotificationType.BUDGET_EXCEEDED)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        
        notificationRepository.save(notif);
    }
}
