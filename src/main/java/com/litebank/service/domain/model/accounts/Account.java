package com.litebank.service.domain.model.accounts;

import com.litebank.service.domain.model.Aggregate;
import com.litebank.service.domain.model.accounts.events.AccountCreditedEvent;
import com.litebank.service.domain.model.accounts.events.AccountDebitFailedDueToInsufficientFundsEvent;
import com.litebank.service.domain.model.accounts.events.AccountDebitedEvent;
import com.litebank.service.domain.model.accounts.events.AccountOpenedEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Account extends Aggregate {

    private UUID customerId;
    private BigDecimal balance;

    public Account(UUID id) {
        super(id);
    }

    public Account(UUID id, UUID customerId, BigDecimal balance) {
        super(id);

        var newEvent = new AccountOpenedEvent(id, LocalDateTime.now(ZoneOffset.UTC), getNextVersion(), customerId, balance);
        applyNewEvent(newEvent);
    }

    public boolean tryToDebitAmount(BigDecimal amountToDebit) {
        return tryToDebitAmountFromMoneyTransfer(amountToDebit, null);
    }

    public boolean tryToDebitAmountFromMoneyTransfer(BigDecimal amountToDebit, UUID moneyTransferId) {
        if (balance.compareTo(amountToDebit) < 0) {
            var failedEvent = new AccountDebitFailedDueToInsufficientFundsEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion(), amountToDebit, balance, moneyTransferId);
            applyNewEvent(failedEvent);

            return false;
        }

        var debitEvent = new AccountDebitedEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion(), amountToDebit, moneyTransferId);
        applyNewEvent(debitEvent);

        return true;
    }

    public void creditAmount(BigDecimal amountToCredit) {
        creditAmountFromMoneyTransfer(amountToCredit, null);
    }

    public void creditAmountFromMoneyTransfer(BigDecimal amountToCredit, UUID moneyTransferId) {
        var creditEvent = new AccountCreditedEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion(), amountToCredit, moneyTransferId);
        applyNewEvent(creditEvent);
    }

    public void apply(AccountOpenedEvent event) {
        this.customerId = event.getCustomerId();
        this.balance = event.getBalance();
    }

    public void apply(AccountDebitedEvent event) {
        this.balance = this.balance.subtract(event.getAmount());
    }

    public void apply(AccountCreditedEvent event) {
        this.balance = this.balance.add(event.getAmount());
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
