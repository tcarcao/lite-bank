package com.litebank.webserver.domain.model.moneytransfers;

import com.litebank.webserver.domain.model.exceptions.InvalidAmountMoneyTransferException;
import com.litebank.webserver.domain.model.exceptions.MoneyTransferOriginEqualToDestinationException;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferCreatedEvent;
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
}