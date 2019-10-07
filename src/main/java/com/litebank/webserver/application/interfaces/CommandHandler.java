package com.litebank.webserver.application.interfaces;

public interface CommandHandler<T extends Command> {
    void execute(T command);
}
