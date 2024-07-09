package com.example.erp.entity.message1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void handleChatMessage(@Payload ChatMessage message) {
        
        messagingTemplate.convertAndSendToUser(message.getSender(), "/queue/messages", message);

        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/messages", message);
    }
}


