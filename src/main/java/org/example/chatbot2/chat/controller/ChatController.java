package org.example.chatbot2.chat.controller;

import org.example.chatbot2.chat.api.ChatDtos;
import org.example.chatbot2.chat.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/conversations")
    public List<ChatDtos.ConversationResponse> conversations(@RequestHeader("X-User-Id") String userId) {
        return chatService.listConversations(userId);
    }

    @GetMapping("/conversations/{conversationId}")
    public ChatDtos.ChatResponse conversation(@RequestHeader("X-User-Id") String userId,
                                               @PathVariable String conversationId) {
        return chatService.getConversation(userId, conversationId);
    }

    @PostMapping("/chat")
    public ChatDtos.ChatResponse chat(@RequestHeader("X-User-Id") String userId,
                                      @RequestBody ChatDtos.SendMessageRequest request) {
        return chatService.send(userId, request);
    }
}
