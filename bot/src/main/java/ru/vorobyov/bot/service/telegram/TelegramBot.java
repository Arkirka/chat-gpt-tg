package ru.vorobyov.bot.service.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vorobyov.bot.configuration.BotConfig;

import java.util.Arrays;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    private final UpdateReceiver updateReceiver;

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public TelegramBot(BotConfig config, UpdateReceiver updateReceiver) {
        super(config.token());
        this.config = config;
        this.updateReceiver = updateReceiver;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage sendMessage = updateReceiver.handleUpdate(update);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                String message = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();

                logger.error("Error sending message [" + message + "] to [" + chatId + "]. Exception: "
                        + e.getMessage() + "Stacktrace: " + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.name();
    }
}
