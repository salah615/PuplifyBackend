package com.example.puplify.Services;
import com.example.puplify.Entities.Notification;

import java.util.List;

public interface INotificationService {
    List<Notification> getAllNotifications();

    boolean markAllAsRead();

    Notification getNotificationById(Long id);

    Notification createNotification(Notification notification);

    Notification updateNotification(Long id, Notification notificationDetails);

    void deleteNotification(Long id);
}
