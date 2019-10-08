package com.litebank.webserver.domain.model.exceptions;

public class InvalidAmountMoneyTransferException extends Exception {
    public InvalidAmountMoneyTransferException(String errorMessage) {
        super(errorMessage);
    }
}
