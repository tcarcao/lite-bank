package com.litebank.webserver.presentation.api.di;

import com.litebank.webserver.application.commands.accounts.*;
import com.litebank.webserver.application.commands.moneytransfers.*;
import com.litebank.webserver.application.interfaces.cqrs.*;

public class CommandQueriesFactoryFromDI implements CommandQueriesFactory {

    @Override
    public <T extends Command> CommandHandler<T> getCommandHandler(Class<T> classType) {
        CommandHandler commandHandler = null;

        if (classType == CompleteMoneyTransferCommand.class) {
            commandHandler =  new CompleteMoneyTransferCommandHandler(DI.getInstance().getMoneyTransferRepository());
        }
        else if (classType == CreditAccountFromTransferCommand.class) {
            commandHandler =  new CreditAccountFromTransferCommandHandler(DI.getInstance().getMoneyTransferRepository(), DI.getInstance().getAccountRepository());
        }
        else if (classType == DebitAccountFromTransferCommand.class) {
            commandHandler =  new DebitAccountFromTransferCommandHandler(DI.getInstance().getAccountRepository());
        }
        else if (classType == FailMoneyTransferDueToInsufficientFundsCommand.class) {
            commandHandler =  new FailMoneyTransferDueToInsufficientFundsCommandHandler(DI.getInstance().getMoneyTransferRepository());
        }

        return commandHandler;
    }

    @Override
    public <T extends Command, R> CommandHandlerR<T, R> getCommandHandlerR(Class<T> classType, Class<R> resultType) {
        CommandHandlerR commandHandler = null;

        if (classType == OpenAccountCommand.class) {
            commandHandler =  new OpenAccountCommandHandler(DI.getInstance().getAccountRepository());
        }
        else if (classType == MoneyTransferCommand.class) {
            commandHandler =  new MoneyTransferCommandHandler(DI.getInstance().getAccountRepository(), DI.getInstance().getMoneyTransferRepository());
        }

        return commandHandler;
    }

    @Override
    public <T extends Query, R> QueryHandler<T, R> getQueryHandler(Class<T> classType, Class<R> resultType) {
        throw new RuntimeException();
    }
}
