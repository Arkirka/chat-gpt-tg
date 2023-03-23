package ru.vorobyov.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vorobyov.bot.configuration.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public TelegramBot(BotConfig config) {
        super(config.token());
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageTest = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageTest) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                default -> sendMessage(chatId, "sorry, im lazy");
            }
        }
    }

    private void startCommandReceived(long chatId, String name){
        String answer = "Hi, " + name +", nice";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error sending message [" + message + "] to [" + chatId + "]. Exception: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.name();
    }
}
