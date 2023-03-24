package ru.vorobyov.chatgpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChatGptApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatGptApplication.class, args);
    }
}
