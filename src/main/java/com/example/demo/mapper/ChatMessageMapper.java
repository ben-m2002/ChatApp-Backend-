package com.example.demo.mapper;

import com.example.demo.dtos.ChatMessageDto;
import com.example.demo.models.ChatMessage;
import com.example.demo.repos.ChatMessageRepo;
import com.example.demo.repos.RoomRepo;
import org.springframework.stereotype.Service;


@Service
public class ChatMessageMapper {

    private final RoomRepo roomRepo;
    private final ChatMessageRepo chatMessageRepo;

    public ChatMessageMapper(RoomRepo roomRepo, ChatMessageRepo chatMessageRepo) {
        this.roomRepo = roomRepo;
        this.chatMessageRepo = chatMessageRepo;
    }

    public ChatMessage mapToChatMessage(ChatMessageDto chatMessageDTO) {
       ChatMessage message = ChatMessage.builder()
                .id(chatMessageDTO.id())
                .content(chatMessageDTO.content())
                .sender(chatMessageDTO.sender())
                .type(chatMessageDTO.type())
                .build();
        message.setRoom(roomRepo.findById(chatMessageDTO.roomId()).get());
        return message;
    }

    public ChatMessageDto mapToChatMessageDto(ChatMessage chatMessage) {
       return new ChatMessageDto(chatMessage.getSender(),
               chatMessage.getContent(),
               chatMessage.getType(),
               chatMessage.getRoom().getId(),
               chatMessage.getId());
    }
}
