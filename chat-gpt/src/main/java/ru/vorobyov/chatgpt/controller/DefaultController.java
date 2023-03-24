package ru.vorobyov.chatgpt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                .sendChatMessage("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", request);
        return ResponseEntity.ok(response);
    }
}
