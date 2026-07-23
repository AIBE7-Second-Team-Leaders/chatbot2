package org.example.chatbot2.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @Column(name = "message_id", length = 36, nullable = false)
    private String messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "model_name", length = 100)
    private String modelName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Message() {}

    public Message(Conversation conversation, String role, String content, String modelName) {
        this.conversation = conversation;
        this.role = role;
        this.content = content;
        this.modelName = modelName;
    }

    @PrePersist
    void onCreate() {
        if (messageId == null) messageId = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = Instant.now();
    }

    public String getMessageId() { return messageId; }
    public String getRole() { return role; }
    public String getContent() { return content; }
    public String getModelName() { return modelName; }
    public Instant getCreatedAt() { return createdAt; }
}
