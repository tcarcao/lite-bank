package com.litebank.webserver.domain.model.moneytransfers;

public enum MoneyTransferState {
    INITIAL, DEBITED, CREDITED, SUCCESS, INSUFFICIENT_FUNDS, FAILED_WRONG_STATE
}
