package ru.vorobyov.bot.service.telegram.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vorobyov.bot.client.ChatGptMicroserviceClient;
import ru.vorobyov.bot.dto.ChatMessageResponse;
import ru.vorobyov.bot.service.database.UserService;

public class CommonMessageHandler extends AbstractMessageHandler{
    private final UserService userService;
    private final ChatGptMicroserviceClient gptClient;

    private final Logger logger = LoggerFactory.getLogger(CommonMessageHandler.class);

    public CommonMessageHandler(UserService userService, ChatGptMicroserviceClient gptClient) {
        this.userService = userService;
        this.gptClient = gptClient;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        var user = userService.findByChatId(chatId);
        if (user.isEmpty())
            return createSendMessage(chatId, "Ой, а вас то нет в базе! Чтобы узнать как это исправить введите /start");

        ChatMessageResponse chatMessageResponse = gptClient.sendMessage(
                String.valueOf(user.get().getGptId()),
                String.valueOf(user.get().getCurrentGptId()),
                message
        );

        if (chatMessageResponse.getAdditionalProperties().isEmpty())
            return createSendMessage(chatId, chatMessageResponse.getMessage());

        logger.error("Exception when creating send message: " + chatMessageResponse.getAdditionalProperties().get("error"));

        return createSendMessage(chatId, "Ой, у нас тут ошибка во время отправки... Попробуйте позже \n Получено достижение: Сломал всю систему");
    }
}
