package com.litebank.service.application.commands.accounts.projections;

import com.litebank.service.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.service.domain.model.accounts.projections.AccountProjection;
import com.litebank.service.domain.model.accounts.projections.TransactionType;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreditAccountProjectionCommandHandlerTest {
    @Test
    public void execute_everythingOk() {
        // Arrange
        var accountProjectionRepository = Mockito.mock(AccountProjectionRepository.class);

        var balance = new BigDecimal(100);
        var amount = new BigDecimal(10);
        var accountId = UUID.randomUUID();
        var timestamp = LocalDateTime.now(ZoneOffset.UTC);

        var account = new AccountProjection(accountId, balance);
        when(accountProjectionRepository.getByIdOrDefault(any(UUID.class))).thenReturn(account);

        var command = new CreditAccountProjectionCommand(accountId, amount, timestamp);
        var handler = new CreditAccountProjectionCommandHandler(accountProjectionRepository);

        // Act
        handler.execute(command);

        // Assert
        verify(accountProjectionRepository).getByIdOrDefault(ArgumentMatchers.eq(accountId));

        ArgumentCaptor<AccountProjection> captor = ArgumentCaptor.forClass(AccountProjection.class);
        verify(accountProjectionRepository).save(captor.capture());

        assertEquals(1, captor.getValue().getTransactions().size());

        var transactionProjection = captor.getValue().getTransactions().get(captor.getValue().getTransactions().size() - 1);

        assertEquals(TransactionType.CREDIT, transactionProjection.getType());
    }
}
