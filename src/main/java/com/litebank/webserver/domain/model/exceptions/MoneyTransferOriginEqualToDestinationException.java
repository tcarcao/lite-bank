package com.litebank.webserver.domain.model.exceptions;

public class MoneyTransferOriginEqualToDestinationException extends Exception {
    public MoneyTransferOriginEqualToDestinationException(String errorMessage) {
        super(errorMessage);
    }
}
