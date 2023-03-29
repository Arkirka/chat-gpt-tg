package ru.vorobyov.bot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class User {

    public User() {
    }

    public User(Long chatId, UUID gptId, String name, Long currentGptId) {
        this.chatId = chatId;
        this.gptId = gptId;
        this.name = name;
        this.currentGptId = currentGptId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    private Long chatId;

    @Column(name = "gpt_id", unique = true , nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID gptId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "current_gpt_id", nullable = false)
    private Long currentGptId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public UUID getGptId() {
        return gptId;
    }

    public void setGptId(UUID gptId) {
        this.gptId = gptId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCurrentGptId() {
        return currentGptId;
    }

    public void setCurrentGptId(Long currentGptId) {
        this.currentGptId = currentGptId;
    }
}
