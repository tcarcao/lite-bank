package com.litebank.webserver.application.commands.moneytransfers.projections;

import com.litebank.webserver.application.interfaces.repositories.MoneyTransferProjectionRepository;
import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferProjection;
import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferStateProjection;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class CreateMoneyTransferProjectionCommandHandlerTest {
    @Test
    public void execute_everythingOk() {
        // Arrange
        var moneyTransferProjectionRepository = Mockito.mock(MoneyTransferProjectionRepository.class);

        var moneyTransferId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";

        var command = new CreateMoneyTransferProjectionCommand(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode);
        var handler = new CreateMoneyTransferProjectionCommandHandler(moneyTransferProjectionRepository);

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
        assertEquals(MoneyTransferStateProjection.SCHEDULED, captor.getValue().getState());
    }
}