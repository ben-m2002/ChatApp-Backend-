package com.example.demo.controllers;
import com.example.demo.dtos.ChatMessageDto;
import com.example.demo.models.ChatMessage;
import com.example.demo.services.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


/*
    To avoid chat duplication we have the client keep a set of message IDs that is seeing in the current
    chat session. When a new message is received, the client checks if the message ID is in the set. If it
    is not, the message is added to the chat window and the message ID is added to the set. This way, the
    client will not display the same message twice.
 */
@Controller
public class ChatController {

    private final ChatMessageService chatMessageService;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto dto) {
        return chatMessageService.getNewChatMessageDto(dto);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDto addUser(
            @Payload ChatMessageDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        // add user name into the websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.sender());
        return chatMessageService.getNewChatMessageDto(chatMessage);
    }
}
