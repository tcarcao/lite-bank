package com.litebank.webserver.application.interfaces.cqrs;

public interface QueryHandler<T extends Query, R> {
    R execute(T query);
}
