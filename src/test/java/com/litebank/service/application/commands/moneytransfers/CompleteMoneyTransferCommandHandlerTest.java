package com.litebank.service.application.commands.moneytransfers;

import com.litebank.service.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.service.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.service.domain.model.moneytransfers.MoneyTransfer;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferCreditRecordedEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferDebitRecordedEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferSuccessfulEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompleteMoneyTransferCommandHandlerTest {
    @Test
    public void execute_everythingOk() throws MoneyTransferNotFoundException {
        // Arrange
        var moneyTransferRepository = Mockito.mock(MoneyTransferRepository.class);
        var moneyTransferId = UUID.randomUUID();
        var moneyTransfer = new MoneyTransfer(moneyTransferId);
        moneyTransfer.apply(new MoneyTransferDebitRecordedEvent(UUID.randomUUID(), LocalDateTime.now(ZoneOffset.UTC), 1));
        when(moneyTransferRepository.getById(any(UUID.class))).thenReturn(moneyTransfer);

        var command = new CompleteMoneyTransferCommand(moneyTransferId);
        var handler = new CompleteMoneyTransferCommandHandler(moneyTransferRepository);

        // Act
        handler.execute(command);

        // Assert
        verify(moneyTransferRepository).getById(ArgumentMatchers.eq(moneyTransferId));

        ArgumentCaptor<MoneyTransfer> captor = ArgumentCaptor.forClass(MoneyTransfer.class);
        verify(moneyTransferRepository).save(captor.capture());

        assertEquals(2, captor.getValue().getNewEvents().size());
        var firstEvent = captor.getValue().getNewEvents().get(0);
        var secondEvent = captor.getValue().getNewEvents().get(1);
        assertEquals(MoneyTransferCreditRecordedEvent.class, firstEvent.getClass());
        assertEquals(MoneyTransferSuccessfulEvent.class, secondEvent.getClass());
    }
}