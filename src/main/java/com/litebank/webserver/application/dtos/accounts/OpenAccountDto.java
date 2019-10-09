package com.litebank.webserver.application.dtos.accounts;

import java.math.BigDecimal;
import java.util.UUID;

public class OpenAccountDto {
    private BigDecimal openingAmount;
    private UUID customerId;

    public OpenAccountDto() {
    }

    public OpenAccountDto(BigDecimal openingAmount, UUID customerId) {
        this.openingAmount = openingAmount;
        this.customerId = customerId;
    }

    public BigDecimal getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(BigDecimal openingAmount) {
        this.openingAmount = openingAmount;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}
