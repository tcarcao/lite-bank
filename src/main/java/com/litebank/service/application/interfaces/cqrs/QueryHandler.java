package com.litebank.service.application.interfaces.cqrs;

public interface QueryHandler<T extends Query, R> {
    R execute(T query);
}
