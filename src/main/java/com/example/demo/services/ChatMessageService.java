package com.example.demo.services;


import com.example.demo.dtos.ChatMessageDto;
import com.example.demo.mapper.ChatMessageMapper;
import com.example.demo.models.ChatMessage;
import com.example.demo.repos.ChatMessageRepo;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {

    private final ChatMessageMapper chatMessageMapper;
    private final ChatMessageRepo chatMessageRepo;

    public ChatMessageService(ChatMessageMapper chatMessageMapper, ChatMessageRepo chatMessageRepo) {
        this.chatMessageMapper = chatMessageMapper;
        this.chatMessageRepo = chatMessageRepo;
    }

    private ChatMessage save(ChatMessageDto dto) {
        ChatMessage chatMessage = chatMessageMapper.mapToChatMessage(dto);
        return chatMessageRepo.save(chatMessage);
    }

    /*
        This method is used to get a new chat message DTO.
        It saves the chat message and gives it an ID the client can use.
     */
    public ChatMessageDto getNewChatMessageDto(ChatMessageDto dto){
       ChatMessage message = this.save(dto);
       return chatMessageMapper.mapToChatMessageDto(message);
    }

}
