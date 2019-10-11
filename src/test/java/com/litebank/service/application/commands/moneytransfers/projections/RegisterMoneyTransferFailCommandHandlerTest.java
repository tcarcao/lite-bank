package com.litebank.service.application.commands.moneytransfers.projections;

import com.litebank.service.application.interfaces.repositories.MoneyTransferProjectionRepository;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferProjection;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferStateProjection;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterMoneyTransferFailCommandHandlerTest {
    @Test
    public void execute_everythingOk() {
        // Arrange
        var moneyTransferProjectionRepository = Mockito.mock(MoneyTransferProjectionRepository.class);

        var moneyTransferId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";

        var moneyTransfer = new MoneyTransferProjection(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode, MoneyTransferStateProjection.SCHEDULED);
        when(moneyTransferProjectionRepository.getByIdOrDefault(any(UUID.class))).thenReturn(moneyTransfer);

        var command = new RegisterMoneyTransferFailCommand(moneyTransferId, "reason");
        var handler = new RegisterMoneyTransferFailCommandHandler(moneyTransferProjectionRepository);

        // Act
        handler.execute(command);

        // Assert
        ArgumentCaptor<MoneyTransferProjection> captor = ArgumentCaptor.forClass(MoneyTransferProjection.class);
        verify(moneyTransferProjectionRepository).save(captor.capture());

        assertEquals(moneyTransferId, captor.getValue().getMoneyTransferId());
        assertEquals(amount, captor.getValue().getAmount());
        assertEquals(fromAccountId, captor.getValue().getFromAccountId());
        assertEquals(toAccountId, captor.getValue().getToAccountId());
        assertEquals(currencyCode, captor.getValue().getCurrencyCode());
        assertEquals(MoneyTransferStateProjection.FAILED, captor.getValue().getState());
        assertNotNull(captor.getValue().getError());
        assertEquals("reason", captor.getValue().getError().getCode());
    }
}