package com.litebank.webserver.domain.model.moneytransfers;

import com.litebank.webserver.domain.model.Aggregate;
import com.litebank.webserver.domain.model.Event;
import com.litebank.webserver.domain.model.exceptions.InvalidAmountMoneyTransferException;
import com.litebank.webserver.domain.model.exceptions.MoneyTransferOriginEqualToDestinationException;
import com.litebank.webserver.domain.model.moneytransfers.events.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class MoneyTransfer extends Aggregate {
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String currencyCode;
    private MoneyTransferState state;

    public MoneyTransfer(UUID id) {
        super(id);
    }

    public MoneyTransfer(UUID id, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode) throws MoneyTransferOriginEqualToDestinationException, InvalidAmountMoneyTransferException {
        super(id);

        validateOriginDestination(fromAccountId, toAccountId);

        validateAmount(amount);

        var createdEvent = new MoneyTransferCreatedEvent(id, LocalDateTime.now(ZoneOffset.UTC), getNextVersion(), fromAccountId, toAccountId, amount, currencyCode);
        applyNewEvent(createdEvent);
    }

    public void debitedWithSuccess() {
        Event event;

        if (state == MoneyTransferState.INITIAL) {
            event = new MoneyTransferDebitRecordedEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion());
        }
        else {
            event = new MoneyTransferFailedDueToInvalidStateEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion());
        }

        applyNewEvent(event);
    }

    public void debitedFailedWithInsufficientAmount() {
        Event event;

        if (state == MoneyTransferState.INITIAL) {
            event = new MoneyTransferDebitFailedDueToInsufficientFundsEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion());
        }
        else {
            event = new MoneyTransferFailedDueToInvalidStateEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion());
        }

        applyNewEvent(event);
    }

    public void finishedWithSuccess() {
        if (state == MoneyTransferState.DEBITED) {
            var event1 = new MoneyTransferCreditRecordedEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion());
            applyNewEvent(event1);

            var event2 = new MoneyTransferSuccessfulEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion());
            applyNewEvent(event2);
        }
        else {
            var event = new MoneyTransferFailedDueToInvalidStateEvent(getId(), LocalDateTime.now(ZoneOffset.UTC), getNextVersion());
            applyNewEvent(event);
        }
    }

    public void apply(MoneyTransferCreatedEvent event) {
        this.fromAccountId = event.getFromAccountId();
        this.toAccountId = event.getToAccountId();
        this.amount = event.getAmount();
        this.currencyCode = event.getCurrencyCode();
        this.state = MoneyTransferState.INITIAL;
    }

    public void apply(MoneyTransferDebitRecordedEvent event) {
        this.state = MoneyTransferState.DEBITED;
    }

    public void apply(MoneyTransferCreditRecordedEvent event) {
        this.state = MoneyTransferState.CREDITED;
    }

    public void apply(MoneyTransferDebitFailedDueToInsufficientFundsEvent event) {
        this.state = MoneyTransferState.INSUFFICIENT_FUNDS;
    }

    public void apply(MoneyTransferSuccessfulEvent event) {
        this.state = MoneyTransferState.SUCCESS;
    }

    public void apply(MoneyTransferFailedDueToInvalidStateEvent event) {
        this.state = MoneyTransferState.FAILED_WRONG_STATE;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public MoneyTransferState getState() {
        return state;
    }

    private void validateOriginDestination(UUID fromAccountId, UUID toAccountId) throws MoneyTransferOriginEqualToDestinationException {
        if (fromAccountId.equals(toAccountId)) {
            throw new MoneyTransferOriginEqualToDestinationException("Source account cannot be destination at the same time: " + fromAccountId);
        }
    }

    private void validateAmount(BigDecimal amount) throws InvalidAmountMoneyTransferException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountMoneyTransferException("The amount (" + amount + ") is not valid. Should be >= 0");
        }
    }
}
