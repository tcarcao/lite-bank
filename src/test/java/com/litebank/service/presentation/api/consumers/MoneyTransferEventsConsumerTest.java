package com.litebank.service.presentation.api.consumers;

import com.litebank.service.application.commands.accounts.CreditAccountFromTransferCommand;
import com.litebank.service.application.commands.accounts.DebitAccountFromTransferCommand;
import com.litebank.service.application.commands.moneytransfers.CompleteMoneyTransferCommand;
import com.litebank.service.application.commands.moneytransfers.FailMoneyTransferDueToInsufficientFundsCommand;
import com.litebank.service.application.interfaces.EventBus;
import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.domain.model.Event;
import com.litebank.service.domain.model.accounts.events.AccountCreditedEvent;
import com.litebank.service.domain.model.accounts.events.AccountDebitFailedDueToInsufficientFundsEvent;
import com.litebank.service.domain.model.accounts.events.AccountDebitedEvent;
import com.litebank.service.domain.model.moneytransfers.events.MoneyTransferCreatedEvent;
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

public class MoneyTransferEventsConsumerTest {
    @Captor
    private ArgumentCaptor<Consumer<List<Event>>> consumerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Class> commandHandlerArgumentCaptor;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void moneyTransferEventsConsumer_consumesMoneyTransferCreatedEvent_shouldCallDebitAccountFromTransferCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(1);
        var currencyCode = "EUR";
        var event = new MoneyTransferCreatedEvent(moneyTransferId, LocalDateTime.now(ZoneOffset.UTC), 1, fromAccountId, toAccountId, amount, currencyCode);
        var moneyTransferEvents = new ArrayList<Event>();

        moneyTransferEvents.add(event);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new MoneyTransferEventsConsumer(eventBus, moneyTransfersStreamId, accountsStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(new ArrayList<>());
        moneyTransfersStreamConsumer.accept(moneyTransferEvents);

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(DebitAccountFromTransferCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void moneyTransferEventsConsumer_consumesAccountDebitedEvent_shouldCallCreditAccountFromTransferCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var amountToDebit = new BigDecimal(0.5);
        var event = new AccountDebitedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToDebit, moneyTransferId);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(event);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new MoneyTransferEventsConsumer(eventBus, moneyTransfersStreamId, accountsStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(CreditAccountFromTransferCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void moneyTransferEventsConsumer_consumesAccountDebitedEventNotFromTransfer_shouldNotCallCommand() {
        // Arrange
        var accountId = UUID.randomUUID();
        var amountToDebit = new BigDecimal(0.5);
        var event = new AccountDebitedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToDebit);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(event);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new MoneyTransferEventsConsumer(eventBus, moneyTransfersStreamId, accountsStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory, never()).getCommandHandler(any());
    }

    @Test
    public void moneyTransferEventsConsumer_consumesAccountCreditedEvent_shouldCallCompleteMoneyTransferCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var amountToDebit = new BigDecimal(0.5);
        var event = new AccountCreditedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToDebit, moneyTransferId);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(event);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new MoneyTransferEventsConsumer(eventBus, moneyTransfersStreamId, accountsStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(CompleteMoneyTransferCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void moneyTransferEventsConsumer_consumesAccountCreditedEventNotFromTransfer_shouldNotCallCommand() {
        // Arrange
        var accountId = UUID.randomUUID();
        var amountToDebit = new BigDecimal(0.5);
        var event = new AccountCreditedEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToDebit);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(event);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new MoneyTransferEventsConsumer(eventBus, moneyTransfersStreamId, accountsStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory, never()).getCommandHandler(any());
    }

    @Test
    public void moneyTransferEventsConsumer_consumesAccountDebitFailedDueToInsufficientFundsEvent_shouldCallFailMoneyTransferDueToInsufficientFundsCommand() {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var amountToDebit = new BigDecimal(0.5);
        var event = new AccountDebitFailedDueToInsufficientFundsEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToDebit, balance, moneyTransferId);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(event);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new MoneyTransferEventsConsumer(eventBus, moneyTransfersStreamId, accountsStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory).getCommandHandler(commandHandlerArgumentCaptor.capture());

        assertEquals(FailMoneyTransferDueToInsufficientFundsCommand.class, commandHandlerArgumentCaptor.getValue());
    }

    @Test
    public void moneyTransferEventsConsumer_consumesAccountDebitFailedDueToInsufficientFundsEventNotFromTransfer_shouldNotCallCommand() {
        // Arrange
        var accountId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var amountToDebit = new BigDecimal(0.5);
        var event = new AccountDebitFailedDueToInsufficientFundsEvent(accountId, LocalDateTime.now(ZoneOffset.UTC), 1, amountToDebit, balance);
        var accountEvents = new ArrayList<Event>();

        accountEvents.add(event);

        var eventBus = Mockito.mock(EventBus.class);
        var accountsStreamId = "AA";
        var moneyTransfersStreamId = "BB";
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandler.class);

        when(commandQueriesFactory.getCommandHandler(any())).thenReturn(genericCommandHandler);

        var consumer = new MoneyTransferEventsConsumer(eventBus, moneyTransfersStreamId, accountsStreamId, commandQueriesFactory);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Act
        consumer.initialize();

        // Assert
        verify(eventBus, times(2)).subscribeToStream(captor.capture(), consumerArgumentCaptor.capture());
        var moneyTransfersStreamConsumer = consumerArgumentCaptor.getAllValues().get(0);
        var accountsStreamConsumer = consumerArgumentCaptor.getAllValues().get(1);

        accountsStreamConsumer.accept(accountEvents);
        moneyTransfersStreamConsumer.accept(new ArrayList<>());

        verify(commandQueriesFactory, never()).getCommandHandler(any());
    }
}
