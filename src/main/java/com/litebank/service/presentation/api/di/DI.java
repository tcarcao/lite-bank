package com.litebank.service.presentation.api.di;

import com.litebank.service.application.interfaces.EventBus;
import com.litebank.service.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.service.application.interfaces.repositories.AccountRepository;
import com.litebank.service.application.interfaces.repositories.MoneyTransferProjectionRepository;
import com.litebank.service.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.service.infrastructure.data.InMemoryAccountProjectionRepository;
import com.litebank.service.infrastructure.data.InMemoryAccountRepository;
import com.litebank.service.infrastructure.data.InMemoryMoneyMoneyTransferProjectionRepository;
import com.litebank.service.infrastructure.data.InMemoryMoneyTransferRepository;
import com.litebank.service.infrastructure.events.InMemoryEventStore;
import com.litebank.service.presentation.api.consumers.MoneyTransferEventsConsumer;
import com.litebank.service.presentation.api.consumers.ProjectionsEventsConsumer;
import com.litebank.service.presentation.api.controllers.AccountsController;
import com.litebank.service.presentation.api.controllers.MoneyTransfersController;

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

    public static void rebuild() {
        instance = new DI();
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
