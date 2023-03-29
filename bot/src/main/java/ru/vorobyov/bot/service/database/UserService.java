package ru.vorobyov.bot.service.database;

import ru.vorobyov.bot.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<User> findByChatId(Long chatId);

    UUID create(Long chatId, UUID gptId, String name, Long currentGptId);
}
