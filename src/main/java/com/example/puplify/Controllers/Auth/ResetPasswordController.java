package com.example.puplify.Controllers.Auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.puplify.DTO.ChangePasswordRequest;
import com.example.puplify.DTO.ResetPasswordRequest;
import com.example.puplify.Entities.User;
import com.example.puplify.Repositories.UserRepository;
import com.example.puplify.Services.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class ResetPasswordController {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        System.out.println(resetPasswordRequest.getEmail());
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail()).orElse(null);
        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String resetToken = generateResetToken();
        user.setResetToken(resetToken);
        userRepository.save(user);
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset email sent successfully");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByResetToken(changePasswordRequest.getToken()).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (user == null) {
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            response.put("message", "New password cannot be the same as the old password");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setResetToken(null); // Clear the reset token
        userRepository.save(user);

        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    private String generateResetToken() {
        // Generate a unique reset token (e.g., using UUID)
        return UUID.randomUUID().toString();
    }
}

