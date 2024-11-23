package com.example.puplify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.puplify.Entities.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
