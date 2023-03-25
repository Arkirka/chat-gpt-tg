package ru.vorobyov.chatgpt.service;

import java.util.UUID;

public interface ChatService {
    Long create(UUID userId, String theme);
}
