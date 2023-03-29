package ru.vorobyov.chatgpt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vorobyov.chatgpt.client.ChatGptClient;
import ru.vorobyov.chatgpt.dto.Message;
import ru.vorobyov.chatgpt.dto.RegisterTokenRequest;
import ru.vorobyov.chatgpt.dto.RegisterTokenResponse;
import ru.vorobyov.chatgpt.entity.User;
import ru.vorobyov.chatgpt.service.ChatService;
import ru.vorobyov.chatgpt.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final UserService userService;
    private final ChatGptClient chatGptClient;
    private final ChatService chatService;

    public AuthorizationController(UserService userService, ChatGptClient chatGptClient, ChatService chatService) {
        this.userService = userService;
        this.chatGptClient = chatGptClient;
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<?> registerToken(@RequestBody RegisterTokenRequest request ){
        String token = request.getToken();
        if (token == null || token.isEmpty())
            return ResponseEntity.badRequest().build();
        var userOptional = userService.findByToken(token);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            var chat = chatService.findByUserId(user.getId());
            if (chat.isPresent())
                return ResponseEntity.ok(
                        new RegisterTokenResponse(String.valueOf(user.getId()), String.valueOf(chat.get().getId()))
                );
            return new ResponseEntity<>("User exist, but not chat", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var requestAdditionalProperties = chatGptClient
                .sendChatMessage(token, getTestChatMessages())
                .getAdditionalProperties();

        boolean containsError = requestAdditionalProperties.containsKey("error");
        if (containsError) {
            if (requestAdditionalProperties.get("error").equals("401"))
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            else
                return ResponseEntity.internalServerError().build();
        }

        UUID id = userService.create(token);
        if (id != null){
            Long chatId = createChat(id);
            if (chatId == null || chatId == -1L)
                return new ResponseEntity<>("Unable create default chat", HttpStatus.INTERNAL_SERVER_ERROR);

            return ResponseEntity.ok(new RegisterTokenResponse(String.valueOf(id), String.valueOf(chatId)));
        }
        return new ResponseEntity<>("Unable create user", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Long createChat(UUID userId){
        return chatService.create(userId, "First dialog");
    }

    private List<Message> getTestChatMessages(){
        return List.of(
                new Message("user", "test message"),
                new Message("assistant", "test response")
        );
    }
}
