package ru.vorobyov.chatgpt.service;

import org.springframework.stereotype.Service;
import ru.vorobyov.chatgpt.entity.User;
import ru.vorobyov.chatgpt.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID findIdByToken(String token) {
        var user = userRepository.findByToken(token);
        return user.map(User::getId).orElse(null);
    }

    @Override
    public UUID create(String token) {
        return userRepository.save(new User(token)).getId();
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}
