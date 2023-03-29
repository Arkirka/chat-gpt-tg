package ru.vorobyov.bot.service.database;

import org.springframework.stereotype.Service;
import ru.vorobyov.bot.entity.User;
import ru.vorobyov.bot.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    public UUID create(Long chatId, UUID gptId, String name, Long currentGptId) {
        return userRepository.save(new User(chatId, gptId, name, currentGptId)).getId();
    }
}
