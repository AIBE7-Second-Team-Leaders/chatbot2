package org.example.chatbot2.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * NVIDIA NIM 접속 정보와 사용할 채팅 모델 설정.
 *
 * <p>NIM은 OpenAI 호환 API를 제공하므로, 이 설정 객체는 NIM 전용
 * 모델 구성이나 ChatClient를 추가로 만들 때 사용할 수 있다.</p>
 */
@ConfigurationProperties(prefix = "spring.ai.nim")
public record NimProperties(
        String apiKey,
        String baseUrl,
        Chat chat
) {

    public record Chat(String model) {
    }
}
