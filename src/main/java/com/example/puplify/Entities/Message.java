package com.example.puplify.Entities;

import lombok.*;
import com.example.puplify.Enums.MessageType;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String content;
    private String sender;
    private MessageType type;
    // Getters and setters
}
