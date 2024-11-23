package com.example.puplify.Entities;

import lombok.Getter;
import lombok.Setter;
import com.example.puplify.Enums.Skillstype;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "user_details")
@Getter
@Setter
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Integer userId;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "resume", columnDefinition = "TEXT")
    private String resume;

    @Column(name = "skills", length = 20)
    private Skillstype skills;

    @Column(name = "experience ", nullable = false)
    private int experience ;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

}
