package com.example.puplify.Services;

import com.example.puplify.Entities.Course;

public interface EmailServiceInterface {
    void sendPasswordResetEmail(String email, String resetToken);
    void sendVerificationEmail(String email, String verificationToken);

}
