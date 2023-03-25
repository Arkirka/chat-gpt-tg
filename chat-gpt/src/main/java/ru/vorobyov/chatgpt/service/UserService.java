package ru.vorobyov.chatgpt.service;

import ru.vorobyov.chatgpt.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UUID findIdByToken(String token);
    UUID create(String token);
    Optional<User> findByToken(String token);
    Optional<User> findById(UUID id);
}
