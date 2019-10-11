package com.litebank.service.domain.model.accounts.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountProjection {
    private final UUID accountId;
    private final List<TransactionProjection> transactions;
    private BigDecimal balance;

    public AccountProjection(UUID accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public void credit(BigDecimal amount, LocalDateTime timestamp) {
        this.balance = this.balance.add(amount);
        this.transactions.add(new TransactionProjection(amount, TransactionType.CREDIT, timestamp));
    }

    public void debit(BigDecimal amount, LocalDateTime timestamp) {
        this.balance = this.balance.subtract(amount);
        this.transactions.add(new TransactionProjection(amount, TransactionType.DEBIT, timestamp));
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<TransactionProjection> getTransactions() {
        return transactions;
    }
}
