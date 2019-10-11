package com.litebank.service.application.queries;

import com.litebank.service.application.interfaces.repositories.MoneyTransferProjectionRepository;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferProjection;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferStateProjection;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetMoneyTransferProjectionQueryHandlerTest {
    @Test
    public void execute_everythingOk() {
        // Arrange
        var moneyTransferProjectionRepository = Mockito.mock(MoneyTransferProjectionRepository.class);

        var moneyTransferId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";
        var balance = new BigDecimal(10);

        var moneyTransferProjection = new MoneyTransferProjection(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode, MoneyTransferStateProjection.SCHEDULED);
        when(moneyTransferProjectionRepository.getByIdOrDefault(any(UUID.class))).thenReturn(moneyTransferProjection);

        var command = new GetMoneyTransferProjectionQuery(moneyTransferId);
        var handler = new GetMoneyTransferProjectionQueryHandler(moneyTransferProjectionRepository);

        // Act
        var result = handler.execute(command);

        // Assert
        verify(moneyTransferProjectionRepository).getByIdOrDefault(moneyTransferId);

        assertNotNull(result);
        assertEquals(moneyTransferId, result.getMoneyTransferId());
        assertEquals(fromAccountId, result.getFromAccountId());
        assertEquals(toAccountId, result.getToAccountId());
        assertEquals(MoneyTransferStateProjection.SCHEDULED, result.getState());
    }
}