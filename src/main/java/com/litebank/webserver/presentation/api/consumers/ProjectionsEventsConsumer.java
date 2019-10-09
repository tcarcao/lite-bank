package com.litebank.webserver.presentation.api.consumers;

import com.litebank.webserver.application.commands.accounts.projections.CreditAccountProjectionCommand;
import com.litebank.webserver.application.commands.accounts.projections.DebitAccountProjectionCommand;
import com.litebank.webserver.application.commands.accounts.projections.OpenAccountProjectionCommand;
import com.litebank.webserver.application.commands.moneytransfers.projections.CreateMoneyTransferProjectionCommand;
import com.litebank.webserver.application.commands.moneytransfers.projections.RegisterMoneyTransferFailCommand;
import com.litebank.webserver.application.commands.moneytransfers.projections.RegisterMoneyTransferFinishedCommand;
import com.litebank.webserver.application.interfaces.EventBus;
import com.litebank.webserver.domain.model.accounts.events.AccountCreditedEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountDebitedEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountOpenedEvent;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferCreatedEvent;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferDebitFailedDueToInsufficientFundsEvent;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferFailedDueToInvalidStateEvent;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferSuccessfulEvent;
import com.litebank.webserver.presentation.api.di.CommandQueriesFactory;

public class ProjectionsEventsConsumer {
    private final EventBus eventBus;
    private final String accountsStreamId;
    private final String moneyTransfersStreamId;
    private final CommandQueriesFactory commandQueriesFactory;

    public ProjectionsEventsConsumer(EventBus eventBus, String accountsStreamId, String moneyTransfersStreamId, CommandQueriesFactory commandQueriesFactory) {
        this.eventBus = eventBus;
        this.accountsStreamId = accountsStreamId;
        this.moneyTransfersStreamId = moneyTransfersStreamId;
        this.commandQueriesFactory = commandQueriesFactory;
    }

    public void initialize() {
        eventBus.subscribeToStream(accountsStreamId, events -> {
            System.out.println("Consuming accounts projection: " + events);

            for (var event : events) {
                if (event instanceof AccountOpenedEvent) {
                    handleAccountOpenedEvent((AccountOpenedEvent) event);
                }
                else if (event instanceof AccountDebitedEvent) {
                    handleAccountDebitedEvent((AccountDebitedEvent) event);
                }
                else if (event instanceof AccountCreditedEvent) {
                    handleAccountCreditedEvent((AccountCreditedEvent) event);
                }
            }
        });

        eventBus.subscribeToStream(moneyTransfersStreamId, events -> {
            System.out.println("Consuming accounts projection: " + events);

            for (var event : events) {
                if (event instanceof MoneyTransferCreatedEvent) {
                    handleMoneyTransferCreatedEvent((MoneyTransferCreatedEvent) event);
                }
                else if (event instanceof MoneyTransferDebitFailedDueToInsufficientFundsEvent) {
                    handleMoneyTransferDebitFailedDueToInsufficientFundsEvent((MoneyTransferDebitFailedDueToInsufficientFundsEvent) event);
                }
                else if (event instanceof MoneyTransferSuccessfulEvent) {
                    handleMoneyTransferSuccessfulEvent((MoneyTransferSuccessfulEvent) event);
                }
                else if (event instanceof MoneyTransferFailedDueToInvalidStateEvent) {
                    handleMoneyTransferFailedDueToInvalidStateEvent((MoneyTransferFailedDueToInvalidStateEvent) event);
                }
            }
        });
    }

    private void handleAccountOpenedEvent(AccountOpenedEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(OpenAccountProjectionCommand.class);
        var command = new OpenAccountProjectionCommand(event.getAggregateId(), event.getBalance());

        handler.execute(command);
    }

    private void handleAccountDebitedEvent(AccountDebitedEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(DebitAccountProjectionCommand.class);
        var command = new DebitAccountProjectionCommand(event.getAggregateId(), event.getAmount(), event.getTimestamp());

        handler.execute(command);
    }

    private void handleAccountCreditedEvent(AccountCreditedEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(CreditAccountProjectionCommand.class);
        var command = new CreditAccountProjectionCommand(event.getAggregateId(), event.getAmount(), event.getTimestamp());

        handler.execute(command);
    }

    private void handleMoneyTransferCreatedEvent(MoneyTransferCreatedEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(CreateMoneyTransferProjectionCommand.class);
        var command = new CreateMoneyTransferProjectionCommand(event.getAggregateId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount(), event.getCurrencyCode());

        handler.execute(command);
    }

    private void handleMoneyTransferDebitFailedDueToInsufficientFundsEvent(MoneyTransferDebitFailedDueToInsufficientFundsEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(RegisterMoneyTransferFailCommand.class);
        var command = new RegisterMoneyTransferFailCommand(event.getAggregateId(), "InsufficientFunds");

        handler.execute(command);
    }

    private void handleMoneyTransferSuccessfulEvent(MoneyTransferSuccessfulEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(RegisterMoneyTransferFinishedCommand.class);
        var command = new RegisterMoneyTransferFinishedCommand(event.getAggregateId());

        handler.execute(command);
    }

    private void handleMoneyTransferFailedDueToInvalidStateEvent(MoneyTransferFailedDueToInvalidStateEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(RegisterMoneyTransferFailCommand.class);
        var command = new RegisterMoneyTransferFailCommand(event.getAggregateId(), "InvalidState");

        handler.execute(command);
    }
}
