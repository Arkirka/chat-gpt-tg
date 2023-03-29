package ru.vorobyov.bot.service.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractMessageHandler {
    public abstract SendMessage handle(Update update);

     SendMessage createSendMessage(long chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }
}
