package com.bd.assignment2.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/messages")
    public void chat(ChatRequest chatRequest) {
        simpMessagingTemplate.convertAndSend("/subscribe/rooms/"+chatRequest.getRoomId(), chatRequest.getMessage());
    }
}
