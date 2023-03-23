package ru.vorobyov.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
public record BotConfig(String name, String token) {}
