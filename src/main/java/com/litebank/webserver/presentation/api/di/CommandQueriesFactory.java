package com.litebank.webserver.presentation.api.di;

import com.litebank.webserver.application.interfaces.cqrs.*;

public interface CommandQueriesFactory {
    <T extends Command> CommandHandler<T> getCommandHandler(Class<T> classType);

    <T extends Command, R> CommandHandlerR<T, R> getCommandHandlerR(Class<T> classType, Class<R> resultType);

    <T extends Query, R> QueryHandler<T, R> getQueryHandler(Class<T> classType, Class<R> resultType);
}
