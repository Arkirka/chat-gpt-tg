package ru.vorobyov.bot.service.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vorobyov.bot.client.ChatGptMicroserviceClient;
import ru.vorobyov.bot.service.database.UserService;
import ru.vorobyov.bot.service.telegram.handler.AbstractMessageHandler;
import ru.vorobyov.bot.service.telegram.handler.CommonMessageHandler;
import ru.vorobyov.bot.service.telegram.handler.RegisterCommandMessageHandler;
import ru.vorobyov.bot.service.telegram.handler.StartCommandMessageHandler;

@Component
public class UpdateReceiver {
    private final UserService userService;
    private final ChatGptMicroserviceClient gptClient;

    public UpdateReceiver(UserService userService, ChatGptMicroserviceClient gptClient) {
        this.userService = userService;
        this.gptClient = gptClient;
    }

    public SendMessage handleUpdate(Update update){
        String messageTest = update.getMessage().getText();
        AbstractMessageHandler messageHandler;
        if (messageTest.startsWith("/"))
            messageHandler = getCommandChain();
        else
            messageHandler = new CommonMessageHandler(userService, gptClient);

        return messageHandler.handle(update);
    }

    private AbstractMessageHandler getCommandChain(){
        return new StartCommandMessageHandler(
                new RegisterCommandMessageHandler(null, userService, gptClient)
            , userService);
    }
}
