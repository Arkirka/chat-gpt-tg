package ru.vorobyov.bot.service.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vorobyov.bot.service.database.UserService;

public class StartCommandMessageHandler extends AbstractCommandMessageHandler{
    private final UserService userService;
    public StartCommandMessageHandler(AbstractCommandMessageHandler next, UserService userService) {
        super(next);
        this.userService = userService;
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        var user = update.getMessage().getFrom();
        String userName = user.getUserName();
        if (message.startsWith("/start")){
            return startCommandReceived(
                    update.getMessage().getChatId(),
                    (userName != null) ?
                            userName :
                            String.format("%s %s", user.getLastName(), user.getFirstName()));
        } else if (next != null){
            return next.handle(update);
        }else {
            return createSendMessage(chatId, "Ех ты... Команда введена неверно");
        }
    }

    private SendMessage startCommandReceived(long chatId, String name){
        if (userService.findByChatId(chatId).isEmpty())
            return createSendMessage(chatId,
                    "Приветствую, ещё не великий " + name +
                            ", который ещё не познал всех удовольствий моего функционала! \n " +
                            "Чтобы примкнуть к ряду счастливцев, которые могут мной пользоваться " +
                            "нужно ввести священный openAI токен в форме: /registerMePlease token");
        else
            return createSendMessage(chatId,
                    "Приветствую, теперь великий " + name +
                            ", который уже имеет возможность познать все удовольствия моего функционала! \n " +
                            "Просто напишите сообщение и получите на него более-менее умный по меркам современности ответ! \n" +
                            "Если вам нужны сведения о моих командах или возможностях просто введите /heeeeelp");
    }
}
