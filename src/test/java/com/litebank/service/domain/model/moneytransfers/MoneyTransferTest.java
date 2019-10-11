package com.litebank.service.domain.model.moneytransfers;

import com.litebank.service.domain.model.exceptions.InvalidAmountMoneyTransferException;
import com.litebank.service.domain.model.exceptions.MoneyTransferOriginEqualToDestinationException;
import com.litebank.service.domain.model.moneytransfers.events.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MoneyTransferTest {
    @Test
    public void createMoneyTransfer_shouldReturnAccountOpenedEvent() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode);

        // Assert
        assertEquals(1, moneyTransfer.getNewEvents().size());
        assertEquals(MoneyTransferCreatedEvent.class, moneyTransfer.getNewerEvent().get().getClass());
    }

    @Test(expected = MoneyTransferOriginEqualToDestinationException.class)
    public void createMoneyTransfer_originEqualToDestination_shouldThrowMoneyTransferOriginEqualToDestinationException() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = fromAccountId;
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode);
        fail();
    }

    @Test(expected = InvalidAmountMoneyTransferException.class)
    public void createMoneyTransfer_invalidAmount_shouldThrowMoneyTransferOriginEqualToDestinationException() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(0);
        var currencyCode = "EUR";

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode);
        fail();
    }

    @Test
    public void debitedWithSuccess_stateOk_shouldReturnMoneyTransferDebitRecordedEvent() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";

        var moneyTransfer = new MoneyTransfer(moneyTransferId);
        moneyTransfer.apply(new MoneyTransferCreatedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1, fromAccountId, toAccountId, amount, currencyCode));

        // Act
        moneyTransfer.debitedWithSuccess();

        // Assert
        assertEquals(1, moneyTransfer.getNewEvents().size());
        assertEquals(MoneyTransferDebitRecordedEvent.class, moneyTransfer.getNewerEvent().get().getClass());
        assertEquals(MoneyTransferState.DEBITED, moneyTransfer.getState());
    }

    @Test
    public void debitedWithSuccess_stateNotOk_shouldReturnMoneyTransferFailedDueToInvalidStateEvent() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        // Act
        moneyTransfer.debitedWithSuccess();

        // Assert
        assertEquals(1, moneyTransfer.getNewEvents().size());
        assertEquals(MoneyTransferFailedDueToInvalidStateEvent.class, moneyTransfer.getNewerEvent().get().getClass());
        assertEquals(MoneyTransferState.FAILED_WRONG_STATE, moneyTransfer.getState());
    }

    @Test
    public void debitedFailedWithInsufficientAmount_stateOk_shouldReturnMoneyTransferDebitFailedDueToInsufficientFundsEvent() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";

        var moneyTransfer = new MoneyTransfer(moneyTransferId);
        moneyTransfer.apply(new MoneyTransferCreatedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1, fromAccountId, toAccountId, amount, currencyCode));

        // Act
        moneyTransfer.debitedFailedWithInsufficientAmount();

        // Assert
        assertEquals(1, moneyTransfer.getNewEvents().size());
        assertEquals(MoneyTransferDebitFailedDueToInsufficientFundsEvent.class, moneyTransfer.getNewerEvent().get().getClass());
        assertEquals(MoneyTransferState.INSUFFICIENT_FUNDS, moneyTransfer.getState());
    }

    @Test
    public void debitedFailedWithInsufficientAmount_stateNotOk_shouldReturnMoneyTransferFailedDueToInvalidStateEvent() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        // Act
        moneyTransfer.debitedFailedWithInsufficientAmount();

        // Assert
        assertEquals(1, moneyTransfer.getNewEvents().size());
        assertEquals(MoneyTransferFailedDueToInvalidStateEvent.class, moneyTransfer.getNewerEvent().get().getClass());
        assertEquals(MoneyTransferState.FAILED_WRONG_STATE, moneyTransfer.getState());
    }

    @Test
    public void finishedWithSuccess_stateOk_shouldReturnMoneyTransferDebitFailedDueToInsufficientFundsEvent() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        var moneyTransfer = new MoneyTransfer(moneyTransferId);
        moneyTransfer.apply(new MoneyTransferDebitRecordedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1));

        // Act
        moneyTransfer.finishedWithSuccess();

        // Assert
        assertEquals(2, moneyTransfer.getNewEvents().size());

        var firstEvent = moneyTransfer.getNewEvents().get(0);
        var secondEvent = moneyTransfer.getNewEvents().get(1);

        assertEquals(MoneyTransferCreditRecordedEvent.class, firstEvent.getClass());
        assertEquals(MoneyTransferSuccessfulEvent.class, secondEvent.getClass());
        assertEquals(MoneyTransferState.SUCCESS, moneyTransfer.getState());
    }

    @Test
    public void finishedWithSuccess_stateNotOk_shouldReturnMoneyTransferFailedDueToInvalidStateEvent() throws InvalidAmountMoneyTransferException, MoneyTransferOriginEqualToDestinationException {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        // Act
        moneyTransfer.finishedWithSuccess();

        // Assert
        assertEquals(1, moneyTransfer.getNewEvents().size());
        assertEquals(MoneyTransferFailedDueToInvalidStateEvent.class, moneyTransfer.getNewerEvent().get().getClass());
        assertEquals(MoneyTransferState.FAILED_WRONG_STATE, moneyTransfer.getState());
    }

    @Test
    public void applyMoneyTransferCreatedEvent_everythingOk() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        var event = new MoneyTransferCreatedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1, fromAccountId, toAccountId, amount, currencyCode);

        // Act
        moneyTransfer.apply(event);

        // Assert
        assertEquals(fromAccountId, moneyTransfer.getFromAccountId());
        assertEquals(toAccountId, moneyTransfer.getToAccountId());
        assertEquals(amount, moneyTransfer.getAmount());
        assertEquals(currencyCode, moneyTransfer.getCurrencyCode());
        assertEquals(MoneyTransferState.INITIAL, moneyTransfer.getState());
    }

    @Test
    public void applyMoneyTransferDebitRecordedEvent_everythingOk() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        var event = new MoneyTransferDebitRecordedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1);

        // Act
        moneyTransfer.apply(event);

        // Assert
        assertEquals(MoneyTransferState.DEBITED, moneyTransfer.getState());
    }

    @Test
    public void applyMoneyTransferCreditRecordedEvent_everythingOk() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        var event = new MoneyTransferCreditRecordedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1);

        // Act
        moneyTransfer.apply(event);

        // Assert
        assertEquals(MoneyTransferState.CREDITED, moneyTransfer.getState());
    }

    @Test
    public void applyMoneyTransferDebitFailedDueToInsufficientFundsEvent_everythingOk() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        var event = new MoneyTransferDebitFailedDueToInsufficientFundsEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1);

        // Act
        moneyTransfer.apply(event);

        // Assert
        assertEquals(MoneyTransferState.INSUFFICIENT_FUNDS, moneyTransfer.getState());
    }

    @Test
    public void applyMoneyTransferSuccessfulEvent_everythingOk() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        var event = new MoneyTransferSuccessfulEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1);

        // Act
        moneyTransfer.apply(event);

        // Assert
        assertEquals(MoneyTransferState.SUCCESS, moneyTransfer.getState());
    }

    @Test
    public void applyMoneyTransferFailedDueToInvalidStateEvent_everythingOk() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();

        // Act
        var moneyTransfer = new MoneyTransfer(moneyTransferId);

        var event = new MoneyTransferFailedDueToInvalidStateEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1);

        // Act
        moneyTransfer.apply(event);

        // Assert
        assertEquals(MoneyTransferState.FAILED_WRONG_STATE, moneyTransfer.getState());
    }
}