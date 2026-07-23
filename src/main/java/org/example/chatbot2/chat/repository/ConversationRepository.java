package org.example.chatbot2.chat.repository;

import org.example.chatbot2.chat.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, String> {
    List<Conversation> findByUser_UserIdOrderByUpdatedAtDesc(String userId);
    Optional<Conversation> findByConversationIdAndUser_UserId(String conversationId, String userId);
}
