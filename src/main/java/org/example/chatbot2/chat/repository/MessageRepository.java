package org.example.chatbot2.chat.repository;

import org.example.chatbot2.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByConversation_ConversationIdOrderByCreatedAtAsc(String conversationId);
}
