package ru.vorobyov.chatgpt.client;

import ru.vorobyov.chatgpt.dto.ChatCompletionsResponse;
import ru.vorobyov.chatgpt.dto.Message;

import java.util.List;

public interface ChatGptClient {
    ChatCompletionsResponse sendChatMessage(String token, List<Message> messages);
}
