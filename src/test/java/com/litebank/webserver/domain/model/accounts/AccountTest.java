package com.litebank.webserver.domain.model.accounts;

import com.litebank.webserver.domain.model.accounts.events.AccountCreditedEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountDebitFailedDueToInsufficientFundsEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountDebitedEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountOpenedEvent;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AccountTest {
    @Test
    public void createAccount_shouldReturnAccountOpenedEvent() {
        // Arrange
        var accountId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var balance = new BigDecimal(10);

        // Act
        var account = new Account(accountId, customerId, balance);

        // Assert
        assertEquals(1, account.getNewEvents().size());
        assertEquals(AccountOpenedEvent.class, account.getNewerEvent().get().getClass());
    }

    @Test
    public void tryToDebitAmountFromMoneyTransfer_balanceWouldBeNegative_shouldReturnFalseAndAddNewFailedEvent() {
        // Arrange
        var accountId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var account = new Account(accountId, customerId, balance);
        var amountToDebit = new BigDecimal(11);
        var moneyTransferId = UUID.randomUUID();

        // Act
        var debitCompleted = account.tryToDebitAmountFromMoneyTransfer(amountToDebit, moneyTransferId);

        // Assert
        assertFalse(debitCompleted);
        assertEquals(2, account.getNewEvents().size());
        assertEquals(AccountDebitFailedDueToInsufficientFundsEvent.class, account.getNewerEvent().get().getClass());
    }

    @Test
    public void tryToDebitAmount_balanceWouldBeNegative_shouldReturnFalseAndAddNewFailedEvent() {
        // Arrange
        var accountId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var account = new Account(accountId, customerId, balance);
        var amountToDebit = new BigDecimal(11);

        // Act
        var debitCompleted = account.tryToDebitAmount(amountToDebit);

        // Assert
        assertFalse(debitCompleted);
        assertEquals(2, account.getNewEvents().size());
        assertEquals(AccountDebitFailedDueToInsufficientFundsEvent.class, account.getNewerEvent().get().getClass());
    }

    @Test
    public void creditAmount_shouldAddAccountCreditedEvent() {
        // Arrange
        var accountId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var account = new Account(accountId, customerId, balance);
        var amountToCredit = new BigDecimal(11);

        // Act
        account.creditAmount(amountToCredit);

        // Assert
        assertEquals(2, account.getNewEvents().size());
        assertEquals(AccountCreditedEvent.class, account.getNewerEvent().get().getClass());
    }

    @Test
    public void creditAmountFromMoneyTransfer_shouldAddAccountCreditedEvent() {
        // Arrange
        var accountId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var account = new Account(accountId, customerId, balance);
        var amountToCredit = new BigDecimal(11);
        var moneyTransferId = UUID.randomUUID();

        // Act
        account.creditAmountFromMoneyTransfer(amountToCredit, moneyTransferId);

        // Assert
        assertEquals(2, account.getNewEvents().size());
        assertEquals(AccountCreditedEvent.class, account.getNewerEvent().get().getClass());
    }

    @Test
    public void applyAccountOpenedEvent_everythingOk() {
        // Arrange
        var accountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var customerId = UUID.randomUUID();
        var account = new Account(accountId);

        var event = new AccountOpenedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, customerId, amount);

        // Act
        account.apply(event);

        // Assert
        assertEquals(customerId, account.getCustomerId());
        assertEquals(amount, account.getBalance());
    }

    @Test
    public void applyAccountDebitedEvent_everythingOk() {
        // Arrange
        var accountId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var amount = new BigDecimal(1);
        var moneyTransferId = UUID.randomUUID();
        var account = new Account(accountId);
        var customerId = UUID.randomUUID();
        account.apply(new AccountOpenedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, customerId, balance));

        var event = new AccountDebitedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amount, moneyTransferId);

        // Act
        account.apply(event);

        // Assert
        assertEquals(new BigDecimal(9), account.getBalance());
    }

    @Test
    public void applyAccountCreditedEvent_everythingOk() {
        // Arrange
        var accountId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var amount = new BigDecimal(1);
        var moneyTransferId = UUID.randomUUID();
        var account = new Account(accountId);
        var customerId = UUID.randomUUID();
        account.apply(new AccountOpenedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, customerId, balance));

        var event = new AccountCreditedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amount, moneyTransferId);

        // Act
        account.apply(event);

        // Assert
        assertEquals(new BigDecimal(11), account.getBalance());
    }
}