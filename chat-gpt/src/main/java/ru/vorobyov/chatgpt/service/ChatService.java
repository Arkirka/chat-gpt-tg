package ru.vorobyov.chatgpt.service;

import ru.vorobyov.chatgpt.entity.Chat;

import java.util.Optional;
import java.util.UUID;

public interface ChatService {
    Long create(UUID userId, String theme);
    boolean checkUserChat(UUID userId, Long chatId);
    Optional<Chat> findById(Long chatId);
    Optional<Chat> findByUserId(UUID userId);
}
