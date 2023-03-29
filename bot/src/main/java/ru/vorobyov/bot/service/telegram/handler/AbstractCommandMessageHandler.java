package ru.vorobyov.bot.service.telegram.handler;

public abstract class AbstractCommandMessageHandler extends AbstractMessageHandler{
    public AbstractCommandMessageHandler next;
    public AbstractCommandMessageHandler(AbstractCommandMessageHandler next) {
        this.next = next;
    }
}
