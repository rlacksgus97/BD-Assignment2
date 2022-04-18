package com.bd.assignment2.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private Long senderId;
    private Long receiverId;
    private Long roomId;
    private String message;
}
