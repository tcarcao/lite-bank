package com.litebank.service.presentation.api.consumers;

import com.litebank.service.application.commands.accounts.projections.CreditAccountProjectionCommand;
import com.litebank.service.application.commands.accounts.projections.DebitAccountProjectionCommand;
import com.litebank.service.application.commands.accounts.projections.OpenAccountProjectionCommand;
import com.litebank.service.application.commands.moneytransfers.projections.CreateMoneyTransferProjectionCommand;
import com.litebank.service.application.commands.moneytransfers.projections.RegisterMoneyTransferFailCommand;
import com.litebank.service.application.commands.moneytransfers.projections.RegisterMoneyTransferFinishedCommand;
import com.litebank.service.application.interfaces.EventBus;
import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.domain.model.Event;
import com.litebank.service.domain.model.accounts.events.AccountCreditedEvent;
import com.litebank.service.domain.model.accounts.events.AccountDebitedEvent;
import com.litebank.service.domain.model.accounts.events.AccountOpenedEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferCreatedEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferDebitFailedDueToInsufficientFundsEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferFailedDueToInvalidStateEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferSuccessfulEvent;
import com.litebank.service.presentation.api.di.CommandQueriesFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProjectionsEventsConsumerTest {
    @Captor
    private ArgumentCaptor<Consumer<List<Event>>> consumerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Class> commandHandlerArgumentCaptor;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void projectionsConsumer_consumesAccountOpenedEvent_shouldCallOpenAccountProjectionCommand() {
        // Arrange
        var accountId = UUID.randomUUID();
        var balance = new BigDecimal(1);
        var accountOpenedEvent = new AccountOpenedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 0, UUID.randomUUID(), balance);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(accountOpenedEvent);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new ProjectionsEventsConsumer(eventBus, accountsStreamId, moneyTransfersStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(OpenAccountProjectionCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void projectionsConsumer_consumesAccountDebitedEvent_shouldCallDebitAccountProjectionCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var amountToDebit = new BigDecimal(0.5);
        var accountDebitEvent = new AccountDebitedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToDebit, moneyTransferId);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(accountDebitEvent);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new ProjectionsEventsConsumer(eventBus, accountsStreamId, moneyTransfersStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(DebitAccountProjectionCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void projectionsConsumer_consumesAccountCreditedEvent_shouldCallCreditAccountProjectionCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var amountToCredit = new BigDecimal(1);
        var accountEvent = new AccountCreditedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToCredit, moneyTransferId);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(accountEvent);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new ProjectionsEventsConsumer(eventBus, accountsStreamId, moneyTransfersStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(CreditAccountProjectionCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void projectionsConsumer_consumesMoneyTransferCreatedEvent_shouldCallCreateMoneyTransferProjectionCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(1);
        var currencyCode = "EUR";
        var moneyTransferEvent = new MoneyTransferCreatedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1, fromAccountId, toAccountId, amount, currencyCode);
        var moneyTransferEvents = new ArrayList<Event>();

        moneyTransferEvents.add(moneyTransferEvent);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new ProjectionsEventsConsumer(eventBus, accountsStreamId, moneyTransfersStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(new ArrayList<>());
        moneyTransfersStreamConsumer.accept(moneyTransferEvents);

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(CreateMoneyTransferProjectionCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void projectionsConsumer_consumesMoneyTransferDebitFailedDueToInsufficientFundsEvent_shouldCallRegisterMoneyTransferFailCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var moneyTransferEvent = new MoneyTransferDebitFailedDueToInsufficientFundsEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1);
        var moneyTransferEvents = new ArrayList<Event>();

        moneyTransferEvents.add(moneyTransferEvent);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new ProjectionsEventsConsumer(eventBus, accountsStreamId, moneyTransfersStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(new ArrayList<>());
        moneyTransfersStreamConsumer.accept(moneyTransferEvents);

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(RegisterMoneyTransferFailCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void projectionsConsumer_consumesMoneyTransferSuccessfulEvent_shouldCallRegisterMoneyTransferFinishedCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var moneyTransferEvent = new MoneyTransferSuccessfulEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 0);
        var moneyTransferEvents = new ArrayList<Event>();

        moneyTransferEvents.add(moneyTransferEvent);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new ProjectionsEventsConsumer(eventBus, accountsStreamId, moneyTransfersStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(new ArrayList<>());
        moneyTransfersStreamConsumer.accept(moneyTransferEvents);

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(RegisterMoneyTransferFinishedCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void projectionsConsumer_consumesMoneyTransferFailedDueToInvalidStateEvent_shouldCallRegisterMoneyTransferFailCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var moneyTransferEvent = new MoneyTransferFailedDueToInvalidStateEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1);
        var moneyTransferEvents = new ArrayList<Event>();

        moneyTransferEvents.add(moneyTransferEvent);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new ProjectionsEventsConsumer(eventBus, accountsStreamId, moneyTransfersStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(new ArrayList<>());
        moneyTransfersStreamConsumer.accept(moneyTransferEvents);

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(RegisterMoneyTransferFailCommand.class, commandHandlerArgumentCaptor.getValue());
    }
}
