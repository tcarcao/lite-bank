package com.litebank.webserver.domain.model.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
