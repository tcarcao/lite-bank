package com.litebank.service.application.dtos.moneytransfers;

public class MoneyTransferValidationDto {
    private String validation;
    private String message;

    public MoneyTransferValidationDto() {
    }

    public MoneyTransferValidationDto(String validation, String message) {
        this.validation = validation;
        this.message = message;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
