package org.example.chatbot2.chat.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.observation.ChatModelObservationConvention;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.setup.OpenAiSetup;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
public class NimChatModelConfig {

    @Bean("nimChatModel")
    ChatModel nimChatModel(
            NimProperties properties,
            ObjectProvider<ChatModelObservationConvention> observationConvention
    ) {

        var client = OpenAiSetup.setupSyncClient(
                properties.baseUrl(),
                properties.apiKey(),
                null,
                null,
                null,
                null,
                false,
                false,
                properties.chat().model(),
                Duration.ofSeconds(60),
                3,
                null,
                Map.of(),
                ObservationRegistry.NOOP,
                null,
                List.of()
        );

        var asyncClient = OpenAiSetup.setupAsyncClient(
                properties.baseUrl(),
                properties.apiKey(),
                null,
                null,
                null,
                null,
                false,
                false,
                properties.chat().model(),
                Duration.ofSeconds(60),
                3,
                null,
                Map.of(),
                ObservationRegistry.NOOP,
                null,
                List.of()
        );

        var model = OpenAiChatModel.builder()
                .openAiClient(client)
                .openAiClientAsync(asyncClient)
                .options(
                        OpenAiChatOptions.builder()
                                .model(properties.chat().model())
                                .build()
                )
                .observationRegistry(ObservationRegistry.NOOP)
                .build();

        observationConvention.ifAvailable(model::setObservationConvention);

        return model;
    }

    @Bean("nimChatClient")
    ChatClient nimChatClient(
            @Qualifier("nimChatModel") ChatModel nimChatModel
    ) {
        return ChatClient.builder(nimChatModel).build();
    }
}