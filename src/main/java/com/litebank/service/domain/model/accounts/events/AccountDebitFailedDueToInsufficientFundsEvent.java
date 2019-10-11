package com.litebank.service.domain.model.accounts.events;

import com.litebank.service.domain.model.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountDebitFailedDueToInsufficientFundsEvent extends Event {

    private final BigDecimal amountToDebit;
    private final BigDecimal balance;
    private UUID moneyTransferId;

    public AccountDebitFailedDueToInsufficientFundsEvent(UUID aggregateId, LocalDateTime timestamp, int version, BigDecimal amountToDebit, BigDecimal balance) {
        super(aggregateId, timestamp, version);

        this.amountToDebit = amountToDebit;
        this.balance = balance;
    }

    public AccountDebitFailedDueToInsufficientFundsEvent(UUID aggregateId, LocalDateTime timestamp, int version, BigDecimal amountToDebit, BigDecimal balance, UUID moneyTransferId) {
        super(aggregateId, timestamp, version);
        this.amountToDebit = amountToDebit;
        this.balance = balance;
        this.moneyTransferId = moneyTransferId;
    }

    public boolean isFromTransfer() {
        return moneyTransferId != null;
    }

    public BigDecimal getAmountToDebit() {
        return amountToDebit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
