package com.example.puplify.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.puplify.Entities.UserDetails;
import com.example.puplify.Services.UserDetailsService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/userdetails")
@CrossOrigin
public class UserDetailsController {
    @Autowired
    private UserDetailsService userDetailsService;
    private static String UPLOAD_DIR = "uploads/";
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Allow ADMIN or STUDENT roles
    public UserDetails createUserDetails(@RequestBody UserDetails userDetails) {
        return userDetailsService.save(userDetails);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Allow ADMIN or STUDENT roles
    public ResponseEntity<UserDetails> getUserDetailsById(@PathVariable Integer id) {
        Optional<UserDetails> userDetails = userDetailsService.findById(id);
        return userDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')") // Allow ADMIN or STUDENT roles
    public ResponseEntity<UserDetails> getUserDetailsByUserId(@PathVariable Integer userId) {
        Optional<UserDetails> userDetails = userDetailsService.findById(userId);
        return userDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Allow ADMIN or STUDENT roles
    public ResponseEntity<UserDetails> updateUserDetails(@PathVariable Integer id, @RequestBody UserDetails userDetails) {
        Optional<UserDetails> optionalUserDetails = userDetailsService.findById(id);
        if (optionalUserDetails.isPresent()) {
            UserDetails existingUserDetails = optionalUserDetails.get();
            existingUserDetails.setUserId(userDetails.getUserId());
            existingUserDetails.setAddress(userDetails.getAddress());
            existingUserDetails.setPhone(userDetails.getPhone());
            existingUserDetails.setResume(userDetails.getResume());
            existingUserDetails.setSkills(userDetails.getSkills());
            existingUserDetails.setExperience(userDetails.getExperience());
            existingUserDetails.setAvatar(userDetails.getAvatar());
            existingUserDetails.setUpdatedAt(userDetails.getUpdatedAt());
            return ResponseEntity.ok(userDetailsService.save(existingUserDetails));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Allow ADMIN or STUDENT roles
    public ResponseEntity<Void> deleteUserDetails(@PathVariable Integer id) {
        if (!userDetailsService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userDetailsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/avatar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<UserDetails> uploadAvatar(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        Optional<UserDetails> optionalUserDetails = userDetailsService.findById(id);
        if (optionalUserDetails.isPresent()) {
            UserDetails userDetails = optionalUserDetails.get();
            try {
                // Validate file type and size (for example, max size 5MB)
                if (!file.getContentType().startsWith("image/") || file.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body(null);
                }

                // Create upload directory if it does not exist
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique file name
                String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);

                // Save the file to the upload directory
                Files.write(filePath, file.getBytes());

                // Update the user details with the avatar path
                userDetails.setAvatar(filePath.toString());
                return ResponseEntity.ok(userDetailsService.save(userDetails));
            } catch (IOException e) {
                e.printStackTrace(); // Log the error for debugging
                return ResponseEntity.status(500).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}