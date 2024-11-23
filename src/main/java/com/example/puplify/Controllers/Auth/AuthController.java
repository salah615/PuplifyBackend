package com.example.puplify.Controllers.Auth;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.puplify.Entities.User;
import com.example.puplify.Enums.Role;
import com.example.puplify.Repositories.UserRepository;
import com.example.puplify.Security.JwtUtil;
import com.example.puplify.Services.CustomUserDetailsService;
import com.example.puplify.Services.EmailService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String usernameOrEmail = loginRequest.get("username");
        String password = loginRequest.get("password");

        // Determine if the provided string is a username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("id", String.valueOf(user.getId()));
        response.put("email", String.valueOf(user.getEmail()));
        response.put("username", String.valueOf(user.getUsername()));
        response.put("role", String.valueOf(user.getRole()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        // Validate if username or email is already registered
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(400).body(Map.of("error", "Username is already registered"));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(400).body(Map.of("error", "Email is already registered"));
        }
        user.setRole(user.getRole());

        // Set password, creation time, etc.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        // Generate a verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        // Save the user
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        // Generate token and return response
        String token = jwtUtil.generateToken(user.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-in-with-token")
    public ResponseEntity<Map<String, String>> signInWithToken(@RequestBody Map<String, String> tokenRequest) {
        String accessToken = tokenRequest.get("accessToken");
        // Validate the token
        if (!jwtUtil.validateToken(accessToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        // Extract username from the token
        String username = jwtUtil.extractUsername(accessToken);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        // Generate a new token
        String newToken = jwtUtil.generateToken(user.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newToken);
        response.put("user", user.getUsername());
        return ResponseEntity.ok(response);
    }


}