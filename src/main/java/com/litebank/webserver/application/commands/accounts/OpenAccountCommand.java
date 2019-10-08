package com.litebank.webserver.application.commands.accounts;

import com.litebank.webserver.application.interfaces.cqrs.Command;

import java.math.BigDecimal;
import java.util.UUID;

public class OpenAccountCommand implements Command {
    private final BigDecimal openingAmount;
    private final UUID customerId;

    public OpenAccountCommand(BigDecimal openingAmount, UUID customerId) {
        this.openingAmount = openingAmount;
        this.customerId = customerId;
    }

    public BigDecimal getOpeningAmount() {
        return openingAmount;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
