package com.example.demo.dtos;

import com.example.demo.others.MessageType;

public record ChatMessageDto(String sender,
                             String content,
                             MessageType type,
                             Integer roomId,
                             Integer id
) {
}
