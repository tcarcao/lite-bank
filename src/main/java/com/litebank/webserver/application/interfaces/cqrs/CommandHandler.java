package com.litebank.webserver.application.interfaces.cqrs;

public interface CommandHandler<T extends Command> {
    void execute(T command);
}
