package com.litebank.webserver.application.commands.accounts;

import com.litebank.webserver.application.interfaces.repositories.AccountRepository;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.domain.model.accounts.events.AccountDebitFailedDueToInsufficientFundsEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountDebitedEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountOpenedEvent;
import com.litebank.webserver.domain.model.exceptions.AccountNotFoundException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DebitAccountFromTransferCommandHandlerTest {
    @Test
    public void execute_everythingOk_accountShouldBeDebited() throws AccountNotFoundException {
        // Arrange
        var accountRepository = Mockito.mock(AccountRepository.class);

        var balance = new BigDecimal(100);
        var amount = new BigDecimal(10);
        var moneyTransferId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var account = new Account(accountId);
        account.apply(new AccountOpenedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 0, UUID.randomUUID(), balance));
        when(accountRepository.getById(any(UUID.class))).thenReturn(account);

        var command = new DebitAccountFromTransferCommand(amount, accountId, moneyTransferId);
        var handler = new DebitAccountFromTransferCommandHandler(accountRepository);

        // Act
        handler.execute(command);

        // Assert
        verify(accountRepository).getById(ArgumentMatchers.eq(accountId));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertEquals(1, captor.getValue().getNewEvents().size());
        assertEquals(AccountDebitedEvent.class, captor.getValue().getNewerEvent().get().getClass());

        var accountDebitedEvent = (AccountDebitedEvent) captor.getValue().getNewerEvent().get();
        assertEquals(moneyTransferId, accountDebitedEvent.getMoneyTransferId());
        assertEquals(amount, accountDebitedEvent.getAmount());
    }

    @Test
    public void execute_noSufficientAmount_insufficientFundsEventShouldBeSaved() throws AccountNotFoundException {
        // Arrange
        var accountRepository = Mockito.mock(AccountRepository.class);

        var amount = new BigDecimal(10);
        var moneyTransferId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var account = new Account(accountId);
        var balance = new BigDecimal(1);
        account.apply(new AccountOpenedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 0, UUID.randomUUID(), balance));
        when(accountRepository.getById(any(UUID.class))).thenReturn(account);

        var command = new DebitAccountFromTransferCommand(amount, accountId, moneyTransferId);
        var handler = new DebitAccountFromTransferCommandHandler(accountRepository);

        // Act
        handler.execute(command);

        // Assert
        verify(accountRepository).getById(ArgumentMatchers.eq(accountId));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertEquals(1, captor.getValue().getNewEvents().size());
        assertEquals(AccountDebitFailedDueToInsufficientFundsEvent.class, captor.getValue().getNewerEvent().get().getClass());

        var insufficientFundsEvent = (AccountDebitFailedDueToInsufficientFundsEvent) captor.getValue().getNewerEvent().get();
        assertEquals(moneyTransferId, insufficientFundsEvent.getMoneyTransferId());
        assertEquals(amount, insufficientFundsEvent.getAmountToDebit());
        assertEquals(balance, insufficientFundsEvent.getBalance());
    }
}