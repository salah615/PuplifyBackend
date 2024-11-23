package com.example.puplify.Controllers.Auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.puplify.Entities.User;
import com.example.puplify.Repositories.UserRepository;
import com.example.puplify.Services.EmailService;

import java.util.UUID;
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class EmailVerificationController {
    private final UserRepository userRepository;
    private final EmailService emailService;
    @PostMapping("/send-verification-email")
    public ResponseEntity<String> sendVerificationEmail(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Generate a verification token
        String verificationToken = UUID.randomUUID().toString();

        // Save the verification token to the user and update the repository
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        // Send the verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return ResponseEntity.ok("Verification email sent successfully");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("token") String token) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Check if the provided token matches the user's verification token
        if (token.equals(user.getVerificationToken())) {
            // If matched, mark the email as verified and save the user
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token");
        }
    }
}

