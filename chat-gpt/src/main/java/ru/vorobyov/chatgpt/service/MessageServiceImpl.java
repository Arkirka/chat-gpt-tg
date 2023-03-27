package ru.vorobyov.chatgpt.service;

import org.springframework.stereotype.Service;
import ru.vorobyov.chatgpt.entity.Message;
import ru.vorobyov.chatgpt.repository.MessageRepository;

import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getMessagesByChatIdOrderedByMessageId(Long chatId) {
        return messageRepository.findByChatIdOrderByIdAsc(chatId);
    }

    @Override
    public List<Message> addAll(List<Message> messages) {
        return messageRepository.saveAll(messages);
    }
}
