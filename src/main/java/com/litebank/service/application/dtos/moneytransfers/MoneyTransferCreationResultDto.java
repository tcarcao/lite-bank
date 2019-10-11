package com.litebank.service.application.dtos.moneytransfers;

import java.util.UUID;

public class MoneyTransferCreationResultDto {
    private UUID moneyTransferId;
    private MoneyTransferValidationDto errorValidation;

    public MoneyTransferCreationResultDto() {
    }

    public MoneyTransferCreationResultDto(UUID moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public MoneyTransferCreationResultDto(MoneyTransferValidationDto errorValidation) {
        this.errorValidation = errorValidation;
    }

    public boolean hasError() {
        return errorValidation != null;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }

    public void setMoneyTransferId(UUID moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public MoneyTransferValidationDto getErrorValidation() {
        return errorValidation;
    }

    public void setErrorValidation(MoneyTransferValidationDto errorValidation) {
        this.errorValidation = errorValidation;
    }
}
