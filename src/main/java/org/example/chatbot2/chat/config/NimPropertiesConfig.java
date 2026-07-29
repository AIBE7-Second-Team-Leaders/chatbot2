package org.example.chatbot2.chat.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * NimProperties를 Spring Bean으로 등록한다.
 */
@Configuration
@EnableConfigurationProperties(NimProperties.class)
public class NimPropertiesConfig {
}
