package ru.vorobyov.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vorobyov.bot.client.ChatGptMicroserviceClient;
import ru.vorobyov.bot.client.ChatGptMicroserviceClientImpl;

@Configuration
public class ClientConfiguration {
    @Bean
    public ChatGptMicroserviceClient chatGptClient(@Value("${client.microservice.chatGpt.base-url}") String baseUrl){
        return new ChatGptMicroserviceClientImpl(baseUrl);
    }
}
