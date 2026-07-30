package org.example.chatbot2.chat.controller;

import jakarta.servlet.http.HttpSession;
import org.example.chatbot2.chat.api.ChatDtos;
import org.example.chatbot2.chat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/conversations")
    public List<ChatDtos.ConversationResponse> conversations(
            HttpSession session) {
        String userId = getLoginUserId(session);
        return chatService.listConversations(userId);
    }

    @GetMapping("/conversations/{conversationId}")
    public ChatDtos.ChatResponse conversation(
            HttpSession session,
            @PathVariable String conversationId) {
        String userId = getLoginUserId(session);
        return chatService.getConversation(userId, conversationId);
    }

    @PostMapping("/chat")
    public ChatDtos.ChatResponse chat(
            HttpSession session,
            @RequestBody ChatDtos.SendMessageRequest request
    ) {
        String userId = getLoginUserId(session);
        return chatService.send(userId, request);
    }

    @PatchMapping("/conversations/{conversationId}")
    public ChatDtos.ConversationResponse renameConversation(
            HttpSession session,
            @PathVariable String conversationId,
            @RequestBody ChatDtos.RenameConversationRequest request
    ) {
        String userId = getLoginUserId(session);
        return chatService.renameConversation(
                userId,
                conversationId,
                request == null ? null : request.title()
        );
    }

    @DeleteMapping("/conversations/{conversationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConversation(
            HttpSession session,
            @PathVariable String conversationId
    ) {
        chatService.deleteConversation(getLoginUserId(session), conversationId);
    }

    private String getLoginUserId(HttpSession session) {
        Object userId = session.getAttribute("loginUserId");
        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "로그인이 필요합니다."
            );
        }

        return userId.toString();
    }
}
