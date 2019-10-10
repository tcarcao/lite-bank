package com.litebank.webserver.presentation.api.consumers;

import com.litebank.webserver.application.commands.accounts.CreditAccountFromTransferCommand;
import com.litebank.webserver.application.commands.accounts.DebitAccountFromTransferCommand;
import com.litebank.webserver.application.commands.moneytransfers.CompleteMoneyTransferCommand;
import com.litebank.webserver.application.commands.moneytransfers.FailMoneyTransferDueToInsufficientFundsCommand;
import com.litebank.webserver.application.interfaces.EventBus;
import com.litebank.webserver.domain.model.accounts.events.AccountCreditedEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountDebitFailedDueToInsufficientFundsEvent;
import com.litebank.webserver.domain.model.accounts.events.AccountDebitedEvent;
import com.litebank.webserver.domain.model.moneytransfers.events.MoneyTransferCreatedEvent;
import com.litebank.webserver.presentation.api.di.CommandQueriesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyTransferEventsConsumer {
    final Logger logger = LoggerFactory.getLogger(MoneyTransferEventsConsumer.class);

    private final EventBus eventBus;
    private final String moneyTransfersStreamId;
    private final String accountsStreamId;
    private final CommandQueriesFactory commandQueriesFactory;

    public MoneyTransferEventsConsumer(EventBus eventBus, String moneyTransfersStreamId, String accountsStreamId, CommandQueriesFactory commandQueriesFactory) {
        this.eventBus = eventBus;
        this.moneyTransfersStreamId = moneyTransfersStreamId;
        this.accountsStreamId = accountsStreamId;
        this.commandQueriesFactory = commandQueriesFactory;
    }

    public void initialize() {
        eventBus.subscribeToStream(moneyTransfersStreamId, events -> {
            logger.info("moneyTransfer :: {}", events);

            for (var event : events) {
                if (event instanceof MoneyTransferCreatedEvent) {
                    handleMoneyTransferCreatedEvent((MoneyTransferCreatedEvent) event);
                }
            }
        });

        eventBus.subscribeToStream(accountsStreamId, events -> {
            logger.info("account :: {}", events);

            for (var event : events) {
                if (event instanceof AccountDebitedEvent) {
                    handleAccountDebitedEvent((AccountDebitedEvent) event);
                }
                else if (event instanceof AccountCreditedEvent) {
                    handleAccountCreditedEvent((AccountCreditedEvent) event);
                }
                else if (event instanceof AccountDebitFailedDueToInsufficientFundsEvent) {
                    handleAccountDebitFailedDueToInsufficientFundsEvent((AccountDebitFailedDueToInsufficientFundsEvent) event);
                }
            }
        });
    }

    private void handleMoneyTransferCreatedEvent(MoneyTransferCreatedEvent event) {
        var handler = commandQueriesFactory.getCommandHandler(DebitAccountFromTransferCommand.class);
        var command = new DebitAccountFromTransferCommand(event.getAmount(), event.getFromAccountId(), event.getAggregateId());

        handler.execute(command);
    }

    private void handleAccountDebitedEvent(AccountDebitedEvent event) {
        if (event.isFromTransfer()) {
            var handler = commandQueriesFactory.getCommandHandler(CreditAccountFromTransferCommand.class);
            var command = new CreditAccountFromTransferCommand(event.getAmount(), event.getMoneyTransferId());

            handler.execute(command);
        }
    }

    private void handleAccountCreditedEvent(AccountCreditedEvent event) {
        if (event.isFromTransfer()) {
            var handler = commandQueriesFactory.getCommandHandler(CompleteMoneyTransferCommand.class);
            var command = new CompleteMoneyTransferCommand(event.getMoneyTransferId());

            handler.execute(command);
        }
    }

    private void handleAccountDebitFailedDueToInsufficientFundsEvent(AccountDebitFailedDueToInsufficientFundsEvent event) {
        if (event.isFromTransfer()) {
            var handler = commandQueriesFactory.getCommandHandler(FailMoneyTransferDueToInsufficientFundsCommand.class);
            var command = new FailMoneyTransferDueToInsufficientFundsCommand(event.getMoneyTransferId());

            handler.execute(command);
        }
    }
}
