package com.litebank.service.application.queries;

import com.litebank.service.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.service.domain.model.accounts.projections.AccountProjection;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAccountProjectionQueryHandlerTest {
    @Test
    public void execute_everythingOk() {
        // Arrange
        var accountProjectionRepository = Mockito.mock(AccountProjectionRepository.class);

        var accountId = UUID.randomUUID();
        var balance = new BigDecimal(10);

        var accountProjection = new AccountProjection(accountId, balance);
        when(accountProjectionRepository.getByIdOrDefault(any(UUID.class))).thenReturn(accountProjection);

        var command = new GetAccountProjectionQuery(accountId);
        var handler = new GetAccountProjectionQueryHandler(accountProjectionRepository);

        // Act
        var result = handler.execute(command);

        // Assert
        verify(accountProjectionRepository).getByIdOrDefault(accountId);

        assertNotNull(result);
        assertEquals(accountId, result.getAccountId());
        assertEquals(balance, result.getBalance());
        assertTrue(result.getTransactions().isEmpty());
    }
}