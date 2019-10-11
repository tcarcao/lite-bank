package com.litebank.service.application.dtos.accounts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDto {
    private UUID accountId;
    private List<TransactionDto> transactions;
    private BigDecimal balance;

    public AccountDto() {
    }

    public AccountDto(UUID accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public AccountDto(UUID accountId, BigDecimal balance, List<TransactionDto> transactions) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactions = transactions;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
