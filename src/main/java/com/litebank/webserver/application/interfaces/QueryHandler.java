package com.litebank.webserver.application.interfaces;

public interface QueryHandler<T extends Query, R> {
    R execute(T query);
}
