package ru.vorobyov.bot.client;

import ru.vorobyov.bot.dto.ChatMessageResponse;
import ru.vorobyov.bot.dto.RegisterTokenResponse;

public interface ChatGptMicroserviceClient {

    RegisterTokenResponse registerToken(String token);
    ChatMessageResponse sendMessage(String gptId, String gptChatId, String message);
}
