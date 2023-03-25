package ru.vorobyov.chatgpt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vorobyov.chatgpt.client.ChatGptClient;
import ru.vorobyov.chatgpt.dto.ChatCompletionsRequest;
import ru.vorobyov.chatgpt.dto.ChatCompletionsResponse;

@RestController
@RequestMapping("/chat")
public class DefaultController {
    private final ChatGptClient chatGptClient;

    public DefaultController(ChatGptClient chatGptClient) {
        this.chatGptClient = chatGptClient;
    }

    @PostMapping
    public ResponseEntity<ChatCompletionsResponse> sendMessage(@RequestBody ChatCompletionsRequest request){
        ChatCompletionsResponse response = chatGptClient
                .sendChatMessage("", request);
        return ResponseEntity.ok(response);
    }
}
