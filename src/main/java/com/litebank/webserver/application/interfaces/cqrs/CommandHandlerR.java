package com.litebank.webserver.application.interfaces.cqrs;

public interface CommandHandlerR<T extends Command, R> {
    R execute(T command);
}
