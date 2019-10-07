package com.litebank.webserver.application.commands.moneytransfers;

import com.litebank.webserver.application.interfaces.repositories.AccountRepository;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.domain.model.exceptions.AccountNotFoundException;
import com.litebank.webserver.domain.model.moneytransfers.MoneyTransfer;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MoneyTransferCommandHandlerTest {
    @Test
    public void execute_everythingOk() throws AccountNotFoundException {
        // Arrange
        var moneyTransferRepository = Mockito.mock(MoneyTransferRepository.class);
        var accountRepository = Mockito.mock(AccountRepository.class);

        var amount = new BigDecimal(10);
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";

        var account = new Account(toAccountId);
        when(accountRepository.getById(any(UUID.class))).thenReturn(account);

        when(moneyTransferRepository.save(any(MoneyTransfer.class))).thenReturn(moneyTransferId);

        var command = new MoneyTransferCommand(fromAccountId, toAccountId, amount, currencyCode);
        var handler = new MoneyTransferCommandHandler(accountRepository, moneyTransferRepository);

        // Act
        var result = handler.execute(command);

        // Assert
        assertNotNull(result);
        assertFalse(result.hasError());
        assertEquals(moneyTransferId, result.getMoneyTransferId());

        verify(accountRepository, times(1)).getById(ArgumentMatchers.eq(fromAccountId));
        verify(accountRepository, times(1)).getById(ArgumentMatchers.eq(toAccountId));

        ArgumentCaptor<MoneyTransfer> captor = ArgumentCaptor.forClass(MoneyTransfer.class);
        verify(moneyTransferRepository).save(captor.capture());
    }

    @Test
    public void execute_fromAccountDoesNotExists_shouldReturnError() throws AccountNotFoundException {
        // Arrange
        var moneyTransferRepository = Mockito.mock(MoneyTransferRepository.class);
        var accountRepository = Mockito.mock(AccountRepository.class);

        var amount = new BigDecimal(10);
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";

        when(accountRepository.getById(fromAccountId)).thenThrow(new AccountNotFoundException("message"));

        var command = new MoneyTransferCommand(fromAccountId, toAccountId, amount, currencyCode);
        var handler = new MoneyTransferCommandHandler(accountRepository, moneyTransferRepository);

        // Act
        var result = handler.execute(command);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasError());
        assertEquals("AccountNotFound", result.getErrorValidation().getValidation());
        assertEquals("message", result.getErrorValidation().getMessage());

        verify(accountRepository, times(1)).getById(ArgumentMatchers.eq(fromAccountId));
        verify(accountRepository, never()).getById(ArgumentMatchers.eq(toAccountId));
        verify(moneyTransferRepository, never()).save(any());
    }

    @Test
    public void execute_toAccountDoesNotExists_shouldReturnError() throws AccountNotFoundException {
        // Arrange
        var moneyTransferRepository = Mockito.mock(MoneyTransferRepository.class);
        var accountRepository = Mockito.mock(AccountRepository.class);

        var amount = new BigDecimal(10);
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";

        when(accountRepository.getById(fromAccountId)).thenReturn(new Account(fromAccountId));
        when(accountRepository.getById(toAccountId)).thenThrow(new AccountNotFoundException("message"));

        var command = new MoneyTransferCommand(fromAccountId, toAccountId, amount, currencyCode);
        var handler = new MoneyTransferCommandHandler(accountRepository, moneyTransferRepository);

        // Act
        var result = handler.execute(command);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasError());
        assertEquals("AccountNotFound", result.getErrorValidation().getValidation());
        assertEquals("message", result.getErrorValidation().getMessage());

        verify(accountRepository, times(1)).getById(ArgumentMatchers.eq(fromAccountId));
        verify(accountRepository, times(1)).getById(ArgumentMatchers.eq(toAccountId));
        verify(moneyTransferRepository, never()).save(any());
    }
}