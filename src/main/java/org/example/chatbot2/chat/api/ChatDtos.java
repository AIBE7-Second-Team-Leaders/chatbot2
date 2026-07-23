package org.example.chatbot2.chat.api;

import org.example.chatbot2.chat.domain.Conversation;
import org.example.chatbot2.chat.domain.Message;

import java.time.Instant;
import java.util.List;

public final class ChatDtos {
    private ChatDtos() {}

    public record SendMessageRequest(String message, String conversationId) {}

    public record ConversationResponse(String conversationId, String title, Instant updatedAt) {
        public static ConversationResponse from(Conversation conversation) {
            return new ConversationResponse(conversation.getConversationId(), conversation.getTitle(), conversation.getUpdatedAt());
        }
    }

    public record MessageResponse(String messageId, String role, String content, String modelName, Instant createdAt) {
        public static MessageResponse from(Message message) {
            return new MessageResponse(message.getMessageId(), message.getRole(), message.getContent(), message.getModelName(), message.getCreatedAt());
        }
    }

    public record ChatResponse(ConversationResponse conversation, List<MessageResponse> messages) {}
}
