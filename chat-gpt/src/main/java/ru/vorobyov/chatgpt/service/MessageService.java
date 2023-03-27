package ru.vorobyov.chatgpt.service;

import ru.vorobyov.chatgpt.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByChatIdOrderedByMessageId(Long chatId);
    List<Message> addAll(List<Message> messages);
}
