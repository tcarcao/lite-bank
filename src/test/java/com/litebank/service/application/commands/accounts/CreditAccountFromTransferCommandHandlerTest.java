package com.litebank.service.application.commands.accounts;

import com.litebank.service.application.interfaces.repositories.AccountRepository;
import com.litebank.service.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.service.domain.model.accounts.Account;
import com.litebank.service.domain.model.accounts.events.AccountCreditedEvent;
import com.litebank.service.domain.model.exceptions.AccountNotFoundException;
import com.litebank.service.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.service.domain.model.moneytransfers.MoneyTransfer;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferCreatedEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferDebitRecordedEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreditAccountFromTransferCommandHandlerTest {
    @Test
    public void execute_everythingOk() throws AccountNotFoundException, MoneyTransferNotFoundException {
        // Arrange
        var moneyTransferRepository = Mockito.mock(MoneyTransferRepository.class);
        var accountRepository = Mockito.mock(AccountRepository.class);

        var amount = new BigDecimal(10);
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";

        var moneyTransfer = new MoneyTransfer(moneyTransferId);
        moneyTransfer.apply(new MoneyTransferCreatedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 0, fromAccountId, toAccountId, amount, currencyCode));
        when(moneyTransferRepository.getById(any(UUID.class))).thenReturn(moneyTransfer);
        when(moneyTransferRepository.save(any())).thenReturn(CompletableFuture.completedFuture(moneyTransferId));

        var account = new Account(toAccountId);
        when(accountRepository.getById(any(UUID.class))).thenReturn(account);
        when(accountRepository.save(any())).thenReturn(CompletableFuture.completedFuture(null));

        var command = new CreditAccountFromTransferCommand(amount, moneyTransferId);
        var handler = new CreditAccountFromTransferCommandHandler(moneyTransferRepository, accountRepository);

        // Act
        handler.execute(command);

        // Assert
        verify(moneyTransferRepository).getById(ArgumentMatchers.eq(moneyTransferId));
        verify(accountRepository).getById(ArgumentMatchers.eq(toAccountId));

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        assertEquals(1, accountCaptor.getValue().getNewEvents().size());
        assertEquals(AccountCreditedEvent.class, accountCaptor.getValue().getNewerEvent().get().getClass());

        ArgumentCaptor<MoneyTransfer> moneyTransferCaptor = ArgumentCaptor.forClass(MoneyTransfer.class);
        verify(moneyTransferRepository).save(moneyTransferCaptor.capture());

        assertEquals(1, moneyTransferCaptor.getValue().getNewEvents().size());
        assertEquals(MoneyTransferDebitRecordedEvent.class, moneyTransferCaptor.getValue().getNewerEvent().get().getClass());
    }
}