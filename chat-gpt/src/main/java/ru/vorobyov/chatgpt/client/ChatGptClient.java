package ru.vorobyov.chatgpt.client;

import ru.vorobyov.chatgpt.dto.ChatCompletionsRequest;
import ru.vorobyov.chatgpt.dto.ChatCompletionsResponse;

public interface ChatGptClient {
    ChatCompletionsResponse sendChatMessage(String token, ChatCompletionsRequest request);
}
