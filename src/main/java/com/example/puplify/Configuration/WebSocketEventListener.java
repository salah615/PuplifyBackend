package com.example.puplify.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.example.puplify.Entities.Message;
import com.example.puplify.Enums.MessageType;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagetemplate;
    @EventListener
    public void handleWebSocketDisconnectListener (SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor =StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username!=null) {
            log.info("User Disconnectd :{}");
            var chatMessage = Message.builder().type(MessageType.LEAVE).sender(username).build();
            messagetemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}