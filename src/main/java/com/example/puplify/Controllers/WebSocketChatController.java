package com.example.puplify.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.puplify.Entities.Message;

@Controller
public class WebSocketChatController {


    @MessageMapping("/connect")
    public void connect(@Payload String userId, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("userId", userId);
    }
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Message send(@Payload  Message message) {
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload  Message message , SimpMessageHeaderAccessor simpMessageHeaderAccessor ) {
        simpMessageHeaderAccessor.getSessionAttributes().put("username",message.getSender());
        return  message;
    }
    @GetMapping("/ws/info")
    public ResponseEntity<String> getInfo(@RequestParam("t") String timestamp) {
        // Handle request and return response
        return ResponseEntity.ok("Data");
    }
}