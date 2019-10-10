package com.litebank.webserver.application.dtos.accounts;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {
    private BigDecimal amount;
    private TransactionTypeDto type;
    private LocalDateTime timestamp;

    public TransactionDto() {
    }

    public TransactionDto(BigDecimal amount, TransactionTypeDto type, LocalDateTime timestamp) {
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(TransactionTypeDto type) {
        this.type = type;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionTypeDto getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
