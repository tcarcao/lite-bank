package com.litebank.service.application.interfaces.cqrs;

public interface CommandHandlerR<T extends Command, R> {
    R execute(T command);
}
