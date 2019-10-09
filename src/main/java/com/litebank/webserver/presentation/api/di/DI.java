package com.litebank.webserver.presentation.api.di;

import com.litebank.webserver.application.interfaces.EventBus;
import com.litebank.webserver.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.webserver.application.interfaces.repositories.AccountRepository;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferProjectionRepository;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.webserver.infrastructure.data.InMemoryAccountProjectionRepository;
import com.litebank.webserver.infrastructure.data.InMemoryAccountRepository;
import com.litebank.webserver.infrastructure.data.InMemoryMoneyMoneyTransferProjectionRepository;
import com.litebank.webserver.infrastructure.data.InMemoryMoneyTransferRepository;
import com.litebank.webserver.infrastructure.events.InMemoryEventStore;
import com.litebank.webserver.presentation.api.consumers.MoneyTransferEventsConsumer;
import com.litebank.webserver.presentation.api.consumers.ProjectionsEventsConsumer;
import com.litebank.webserver.presentation.api.controllers.AccountsController;
import com.litebank.webserver.presentation.api.controllers.MoneyTransfersController;

public class DI {
    public static final String ACCOUNT_STREAM_ID = "ACCOUNT_STREAM_ID";
    public static final String MONEY_TRANSFERS_STREAM_ID = "MONEY_TRANSFERS_STREAM_ID";

    private static DI instance;

    private final EventBus eventBus;
    private final AccountRepository accountRepository;
    private final AccountProjectionRepository accountProjectionRepository;
    private final MoneyTransferRepository moneyTransferRepository;
    private final MoneyTransferProjectionRepository moneyTransferProjectionRepository;
    private final CommandQueriesFactory commandQueriesFactory;

    private DI() {
        eventBus = new InMemoryEventStore();
        accountRepository = new InMemoryAccountRepository(eventBus, ACCOUNT_STREAM_ID);
        accountProjectionRepository = new InMemoryAccountProjectionRepository();
        moneyTransferRepository = new InMemoryMoneyTransferRepository(eventBus, MONEY_TRANSFERS_STREAM_ID);
        moneyTransferProjectionRepository = new InMemoryMoneyMoneyTransferProjectionRepository();
        commandQueriesFactory = new CommandQueriesFactoryFromDI();
    }

    public static synchronized DI getInstance(){
        if (instance == null){
            instance = new DI();
        }

        return instance;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public MoneyTransferRepository getMoneyTransferRepository() {
        return moneyTransferRepository;
    }

    public AccountProjectionRepository getAccountProjectionRepository() {
        return accountProjectionRepository;
    }

    public MoneyTransferProjectionRepository getMoneyTransferProjectionRepository() {
        return moneyTransferProjectionRepository;
    }

    public CommandQueriesFactory getCommandQueriesFactory() {
        return commandQueriesFactory;
    }

    public MoneyTransferEventsConsumer getMoneyTransferEventsConsumer() {
        return new MoneyTransferEventsConsumer(eventBus, MONEY_TRANSFERS_STREAM_ID, ACCOUNT_STREAM_ID, commandQueriesFactory);
    }

    public ProjectionsEventsConsumer getProjectionsEventsConsumer() {
        return new ProjectionsEventsConsumer(eventBus, ACCOUNT_STREAM_ID, MONEY_TRANSFERS_STREAM_ID, commandQueriesFactory);
    }

    public MoneyTransfersController getMoneyTransfersController() {
        return new MoneyTransfersController(commandQueriesFactory);
    }

    public AccountsController getAccountsController() {
        return new AccountsController(commandQueriesFactory);
    }
}
