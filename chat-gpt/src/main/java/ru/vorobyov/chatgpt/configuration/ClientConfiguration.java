package ru.vorobyov.chatgpt.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vorobyov.chatgpt.client.ChatGptClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public ChatGptClient chatGptClient(@Value("${client.chatGpt.base-url}") String baseUrl){
        return new ChatGptClient(baseUrl);
    }
}
