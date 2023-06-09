package ru.vorobyov.chatgpt.service;

import org.springframework.stereotype.Service;
import ru.vorobyov.chatgpt.entity.Chat;
import ru.vorobyov.chatgpt.entity.User;
import ru.vorobyov.chatgpt.repository.ChatRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("chatService")
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;

    private final UserService userService;

    public ChatServiceImpl(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public Long create(UUID userId, String theme) {
        Chat chat = new Chat();
        var user = userService.findById(userId);
        if (user.isEmpty())
            return -1L;
        chat.setUser(user.get());
        chat.setTheme(theme);
        return chatRepository.save(chat).getId();
    }

    @Override
    public boolean checkUserChat(UUID userId, Long chatId) {
        Optional<User> optionalUser = userService.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Chat> chatList = user.getChatList();
            if (chatList != null && !chatList.isEmpty()) {
                for (Chat chat : chatList) {
                    if (chat.getId().equals(chatId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Optional<Chat> findById(Long chatId) {
        return chatRepository.findById(chatId);
    }

    @Override
    public Optional<Chat> findByUserId(UUID userId) {
        return chatRepository.findByUserId(userId);
    }
}
