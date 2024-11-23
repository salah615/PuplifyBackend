package com.example.puplify.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "notifications") // Ensure this matches your database table name
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Title is required
    private String title;

    @Column(nullable = false) // Description is required
    private String description;

    @Column(nullable = true) // Link is optional
    private String link;

    @Column(nullable = true) // Read status is optional, default to false
    private boolean isRead = false;

    @Column(nullable = true) // Time is optional, you can set a default value
    private LocalDateTime time;

    // Constructor with all parameters
    public Notification(String title, String message, String link) {
        this.title = title;
        this.description = message;
        this.link = link;
        this.isRead = false ;
        this.time = LocalDateTime.now();
    }

}
