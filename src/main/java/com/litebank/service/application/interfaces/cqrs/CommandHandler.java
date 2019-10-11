package com.litebank.service.application.interfaces.cqrs;

public interface CommandHandler<T extends Command> {
    void execute(T command);
}
