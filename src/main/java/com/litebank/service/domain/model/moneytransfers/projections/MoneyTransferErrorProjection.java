package com.litebank.service.domain.model.moneytransfers.projections;

public class MoneyTransferErrorProjection {
    private final String code;
    private final String message;

    public MoneyTransferErrorProjection(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
