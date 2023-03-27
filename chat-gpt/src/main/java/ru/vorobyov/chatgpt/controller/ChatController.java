package ru.vorobyov.chatgpt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vorobyov.chatgpt.client.ChatGptClient;
import ru.vorobyov.chatgpt.dto.ChatCompletionsResponse;
import ru.vorobyov.chatgpt.dto.ChatMessageRequest;
import ru.vorobyov.chatgpt.dto.Choice;
import ru.vorobyov.chatgpt.dto.Message;
import ru.vorobyov.chatgpt.entity.User;
import ru.vorobyov.chatgpt.service.ChatService;
import ru.vorobyov.chatgpt.service.MessageService;
import ru.vorobyov.chatgpt.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;
    private final ChatGptClient chatGptClient;

    private final MessageService messageService;

    public ChatController(UserService userService,
                          ChatService chatService,
                          ChatGptClient chatGptClient,
                          MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.chatGptClient = chatGptClient;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequest request){
        //check user valid
        // check user has chat
        // create request to chatGpt with chat history and new message
        // get response
        // update chat history on database with new messages
        // * send to client message from chatGpt
        UUID userId = UUID.fromString(request.getUserId());
        Long chatId = Long.valueOf(request.getChatId());
        String userMessage = request.getMessage();

        if (!isUserExist(userId))
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        if (!isChatExist(userId, chatId))
            return new ResponseEntity<>("Chat not found", HttpStatus.NOT_FOUND);
        if (userMessage == null || userMessage.isBlank())
            return new ResponseEntity<>("Message is blank", HttpStatus.BAD_REQUEST);

        var gptResponse = chatGptClient
                .sendChatMessage(
                        getUserToken(userId), getDtoMessagesByChatIdAndMessage(chatId, userMessage)
                );

        if (isGptResponseHasError(gptResponse)){
            if (gptResponse.getAdditionalProperties().get("error").equals("401"))
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            else
                return ResponseEntity.internalServerError().build();
        }

        if (!addNewMessagesToDb(gptResponse, userMessage, chatId))
            return new ResponseEntity<>("Unable to update chat history", HttpStatus.INTERNAL_SERVER_ERROR);

        String responseMessage = gptResponse.getChoices()
                .get(gptResponse.getChoices().size() - 1)
                .getMessage()
                .getContent();
        return ResponseEntity.ok(responseMessage);
    }

    private boolean isUserExist(UUID userId){
        return userService.findById(userId).isPresent();
    }

    private boolean isChatExist(UUID userId, Long chatId){
        return chatService.checkUserChat(userId, chatId);
    }

    private List<Message> getDtoMessagesByChatIdAndMessage(Long chatId, String message){
        List<Message> messages = new java.util.ArrayList<>(
                messageService.getMessagesByChatIdOrderedByMessageId(chatId)
                .stream().map(x -> new Message(x.getRole(), x.getContent())).toList()
        );
        messages.add(new Message("user", message));
        return messages;
    }

    private String getUserToken(UUID userId){
        return userService.findById(userId).orElse(new User()).getToken();
    }

    private boolean isGptResponseHasError(ChatCompletionsResponse gptResponse){
        var additionalProperties = gptResponse.getAdditionalProperties();
        return additionalProperties.containsKey("error");
    }

    private boolean addNewMessagesToDb(ChatCompletionsResponse gptResponse, String userMessage, Long chatId){
        var chatOptional = chatService.findById(chatId);
        if (chatOptional.isEmpty())
            return false;

        List<ru.vorobyov.chatgpt.entity.Message> messages = new java.util.ArrayList<>(List.of(
                new ru.vorobyov.chatgpt.entity.Message("user", userMessage, chatOptional.get())
        ));
        messages.addAll(gptResponse.getChoices()
                .stream()
                .map(Choice::getMessage)
                .map(x -> new ru.vorobyov.chatgpt.entity.Message(x.getRole(), x.getContent(), chatOptional.get()))
                .toList());

        return !messageService.addAll(messages).isEmpty();
    }
}
