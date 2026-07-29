package org.example.chatbot2.chat.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 모델별 ChatClient를 선택해서 사용하는 예시 서비스.
 */
@Service
public class QualifiedChatService {

    private final ChatClient googleGenAiChatClient;
    private final ChatClient openAiChatClient;
    private final ChatClient nimChatClient;
    private final ChatModel googleGenAiChatModel;
    private final ChatModel openAiChatModel;
    private final ChatModel nimChatModel;

    public QualifiedChatService(
            @Qualifier("googleGenAiChatClient") ChatClient googleGenAiChatClient,
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("nimChatClient") ChatClient nimChatClient,
            @Qualifier("googleGenAiChatModel") ChatModel googleGenAiChatModel,
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            @Qualifier("nimChatModel") ChatModel nimChatModel) {
        this.googleGenAiChatClient = googleGenAiChatClient;
        this.openAiChatClient = openAiChatClient;
        this.nimChatClient = nimChatClient;
        this.googleGenAiChatModel = googleGenAiChatModel;
        this.openAiChatModel = openAiChatModel;
        this.nimChatModel = nimChatModel;
    }

    public String askGoogle(String message) {
        return googleGenAiChatClient.prompt(message).call().content();
    }

    public String askOpenAi(String message) {
        return openAiChatClient.prompt(message).call().content();
    }

    public String askNim(String message) {
        return nimChatClient.prompt(message).call().content();
    }

    public String askGoogleWithChatModel(String message) {
        return googleGenAiChatModel.call(message);
    }

    public String askOpenAiWithChatModel(String message) {
        return openAiChatModel.call(message);
    }

    public String askNimWithChatModel(String message) {
        return nimChatModel.call(message);
    }
}
