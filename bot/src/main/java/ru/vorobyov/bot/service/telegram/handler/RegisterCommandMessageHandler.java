package ru.vorobyov.bot.service.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vorobyov.bot.client.ChatGptMicroserviceClient;
import ru.vorobyov.bot.dto.RegisterTokenResponse;
import ru.vorobyov.bot.service.database.UserService;

import java.util.UUID;

public class RegisterCommandMessageHandler extends AbstractCommandMessageHandler{
    private final UserService userService;

    private final ChatGptMicroserviceClient gptClient;

    public RegisterCommandMessageHandler(AbstractCommandMessageHandler next,
                                         UserService userService,
                                         ChatGptMicroserviceClient gptClient) {
        super(next);
        this.userService = userService;
        this.gptClient = gptClient;
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        var user = update.getMessage().getFrom();
        String userName = (user.getUserName() != null) ?
                user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        if (message.startsWith("/registerMePlease")){
            String[] commandAndToken = message.split(" ");

            if (commandAndToken.length == 2)
                return registerCommandReceived(
                    update.getMessage().getChatId(),
                    userName,
                    commandAndToken[1]
                );

        } else if (next != null)
            return next.handle(update);

        return createSendMessage(chatId, "Ех ты... Команда введена неверно");
    }

    private SendMessage registerCommandReceived(long chatId, String name, String token){
        if (userService.findByChatId(chatId).isPresent())
            return createSendMessage(chatId,
                    "Вы уже зарегистрированы вообще-то!");
        else {
            RegisterTokenResponse response = gptClient.registerToken(token);

            UUID userId = userService.create(chatId,
                    UUID.fromString(response.getUserId()),
                    name,
                    Long.parseLong(response.getChatId()));

            return userId != null ?
                    createSendMessage(chatId, "Поздравляю с регистрацией! " +
                            "Теперь снова введите /start чтобы получить вводную информацию. \n " +
                            "Получено достижение: Великий") :
                    createSendMessage(chatId, "Что-то пошло не так! " +
                            "Надеюсь, ты не занимался богохульством вроде ввода неправильного токена...");
        }
    }
}
