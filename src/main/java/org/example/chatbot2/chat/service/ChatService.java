package org.example.chatbot2.chat.service;

import org.example.chatbot2.chat.api.ChatDtos;
import org.example.chatbot2.chat.domain.AppUser;
import org.example.chatbot2.chat.domain.Conversation;
import org.example.chatbot2.chat.domain.Message;
import org.example.chatbot2.chat.repository.AppUserRepository;
import org.example.chatbot2.chat.repository.ConversationRepository;
import org.example.chatbot2.chat.repository.MessageRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChatService {
    private final AppUserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ChatClient groqChatClient;
    private final ChatClient geminiChatClient;
    private final ChatClient nimChatClient;

    public ChatService(AppUserRepository userRepository, ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       @Qualifier("openAiChatClient") ChatClient groqChatClient,
                       @Qualifier("googleGenAiChatClient") ChatClient geminiChatClient,
                       @Qualifier("nimChatClient") ChatClient nimChatClient) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.groqChatClient = groqChatClient;
        this.geminiChatClient = geminiChatClient;
        this.nimChatClient = nimChatClient;
    }

    public List<ChatDtos.ConversationResponse> listConversations(String userId) {
        return conversationRepository.findByUser_UserIdOrderByUpdatedAtDesc(userId).stream()
                .map(ChatDtos.ConversationResponse::from).toList();
    }

    public ChatDtos.ChatResponse getConversation(String userId, String conversationId) {
        Conversation conversation = getOwnedConversation(userId, conversationId);
        List<ChatDtos.MessageResponse> messages = messageRepository
                .findByConversation_ConversationIdOrderByCreatedAtAsc(conversationId).stream()
                .map(ChatDtos.MessageResponse::from).toList();
        return new ChatDtos.ChatResponse(ChatDtos.ConversationResponse.from(conversation), messages);
    }

    @Transactional
    public ChatDtos.ConversationResponse renameConversation(
            String userId,
            String conversationId,
            String title
    ) {
        String normalizedTitle = title == null ? "" : title.trim();
        if (normalizedTitle.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        Conversation conversation = getOwnedConversation(userId, conversationId);
        conversation.rename(normalizedTitle.length() > 200
                ? normalizedTitle.substring(0, 200)
                : normalizedTitle);
        return ChatDtos.ConversationResponse.from(conversationRepository.save(conversation));
    }

    @Transactional
    public void deleteConversation(String userId, String conversationId) {
        Conversation conversation = getOwnedConversation(userId, conversationId);
        conversationRepository.delete(conversation);
    }

    @Transactional
    public ChatDtos.ChatResponse send(String userId, ChatDtos.SendMessageRequest request) {
        String content = request == null || request.message() == null ? "" : request.message().trim();
        if (content.isBlank()) throw new IllegalArgumentException("message must not be blank");
        String model = normalizeModel(request == null ? null : request.model());
        ChatClient chatClient = chatClientFor(model);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found: " + userId));
        Conversation conversation = request.conversationId() == null || request.conversationId().isBlank()
                ? conversationRepository.save(new Conversation(user, createTitle(content)))
                : getOwnedConversation(userId, request.conversationId());

        messageRepository.save(new Message(conversation, "USER", content, null));
        List<Message> history = messageRepository
                .findByConversation_ConversationIdOrderByCreatedAtAsc(conversation.getConversationId());

        String answer = chatClient.prompt()
                .messages(history.stream().map(this::toPromptMessage).toList())
                .call().content();

        messageRepository.save(new Message(conversation, "ASSISTANT", answer, model));
        conversation.touch();
        conversationRepository.save(conversation);
        return getConversation(userId, conversation.getConversationId());
    }

    private ChatClient chatClientFor(String model) {
        return switch (model) {
            case "gemini" -> geminiChatClient;
            case "nim" -> nimChatClient;
            default -> groqChatClient;
        };
    }

    private String normalizeModel(String model) {
        if (model == null || model.isBlank()) return "groq";
        return switch (model.trim().toLowerCase()) {
            case "groq", "openai" -> "groq";
            case "gemini" -> "gemini";
            case "nim" -> "nim";
            default -> throw new IllegalArgumentException("unsupported model: " + model);
        };
    }

    private org.springframework.ai.chat.messages.Message toPromptMessage(Message message) {
        return switch (message.getRole()) {
            case "USER" -> new UserMessage(message.getContent());
            case "ASSISTANT" -> new AssistantMessage(message.getContent());
            default -> new SystemMessage(message.getContent());
        };
    }

    private Conversation getOwnedConversation(String userId, String conversationId) {
        return conversationRepository.findByConversationIdAndUser_UserId(conversationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("conversation not found"));
    }

    private String createTitle(String message) {
        String normalized = message.replaceAll("\\s+", " ").trim();
        return normalized.length() <= 48 ? normalized : normalized.substring(0, 48) + "...";
    }
}
