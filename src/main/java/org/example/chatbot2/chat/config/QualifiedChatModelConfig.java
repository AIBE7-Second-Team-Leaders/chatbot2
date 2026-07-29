package org.example.chatbot2.chat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 여러 ChatModel이 등록된 환경에서 사용할 모델을 명시적으로 선택한다.
 *
 * <p>기존 코드와의 호환성을 위해 OpenAI 모델을 기본 모델로 지정하고,
 * 새 코드에서는 모델과 클라이언트를 각각 qualifier로 주입해
 * Google GenAI, OpenAI, NVIDIA NIM 중 원하는 모델을 선택할 수 있다.</p>
 */
@Configuration
public class QualifiedChatModelConfig {

    @Bean
    @Primary
    ChatModel defaultChatModel(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {
        return openAiChatModel;
    }

    @Bean("googleGenAiChatClient")
    ChatClient googleGenAiChatClient(
            @Qualifier("googleGenAiChatModel") ChatModel googleGenAiChatModel) {
        return ChatClient.builder(googleGenAiChatModel).build();
    }

    @Bean("openAiChatClient")
    @Primary
    ChatClient openAiChatClient(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }

}
