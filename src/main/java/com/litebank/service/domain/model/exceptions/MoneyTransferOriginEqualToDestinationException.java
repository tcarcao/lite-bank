package com.litebank.service.domain.model.exceptions;

public class MoneyTransferOriginEqualToDestinationException extends Exception {
    public MoneyTransferOriginEqualToDestinationException(String errorMessage) {
        super(errorMessage);
    }
}
