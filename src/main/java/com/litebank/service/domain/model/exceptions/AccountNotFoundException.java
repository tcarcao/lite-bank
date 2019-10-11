package com.litebank.service.domain.model.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
