package com.litebank.webserver.application.commands.moneytransfers;

import com.litebank.webserver.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.webserver.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.webserver.domain.model.moneytransfers.MoneyTransfer;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferCreatedEvent;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferDebitFailedDueToInsufficientFundsEvent;
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

public class FailMoneyTransferDueToInsufficientFundsCommandHandlerTest {
    @Test
    public void execute_everythingOk() throws MoneyTransferNotFoundException {
        // Arrange
        var moneyTransferRepository = Mockito.mock(MoneyTransferRepository.class);

        var amount = new BigDecimal(10);
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var currencyCode = "EUR";

        var moneyTransfer = new MoneyTransfer(moneyTransferId);
        moneyTransfer.apply(new MoneyTransferCreatedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 0, fromAccountId, toAccountId, amount, currencyCode));
        when(moneyTransferRepository.getById(any(UUID.class))).thenReturn(moneyTransfer);

        var command = new FailMoneyTransferDueToInsufficientFundsCommand(moneyTransferId);
        var handler = new FailMoneyTransferDueToInsufficientFundsCommandHandler(moneyTransferRepository);

        // Act
        handler.execute(command);

        // Assert
        verify(moneyTransferRepository).getById(ArgumentMatchers.eq(moneyTransferId));

        ArgumentCaptor<MoneyTransfer> captor = ArgumentCaptor.forClass(MoneyTransfer.class);
        verify(moneyTransferRepository).save(captor.capture());

        assertEquals(1, captor.getValue().getNewEvents().size());
        assertEquals(MoneyTransferDebitFailedDueToInsufficientFundsEvent.class, captor.getValue().getNewerEvent().get().getClass());
    }
}