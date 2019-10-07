package com.litebank.webserver.domain.model.exceptions;

public class MoneyTransferNotFoundException extends Exception {
    public MoneyTransferNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
