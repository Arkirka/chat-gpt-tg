package ru.vorobyov.chatgpt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vorobyov.chatgpt.client.ChatGptClient;
import ru.vorobyov.chatgpt.dto.ChatCompletionsResponse;
import ru.vorobyov.chatgpt.dto.Message;

import java.util.List;

@RestController
@RequestMapping("/test")
public class DefaultController {
    private final ChatGptClient chatGptClient;

    public DefaultController(ChatGptClient chatGptClient) {
        this.chatGptClient = chatGptClient;
    }

    @PostMapping
    public ResponseEntity<ChatCompletionsResponse> sendMessage(){
        ChatCompletionsResponse response = chatGptClient
                .sendChatMessage("", getTestChatMessages());
        return ResponseEntity.ok(response);
    }

    private List<Message> getTestChatMessages(){
        return List.of(
                new Message("user", "test message"),
                new Message("assistant", "test response")
        );
    }
}
