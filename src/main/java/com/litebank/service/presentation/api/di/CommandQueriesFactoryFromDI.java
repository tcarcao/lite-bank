package com.litebank.service.presentation.api.di;

import com.litebank.service.application.commands.accounts.*;
import com.litebank.service.application.commands.accounts.projections.*;
import com.litebank.service.application.commands.moneytransfers.*;
import com.litebank.service.application.commands.moneytransfers.projections.*;
import com.litebank.service.application.interfaces.cqrs.*;
import com.litebank.service.application.queries.GetAccountProjectionQuery;
import com.litebank.service.application.queries.GetAccountProjectionQueryHandler;
import com.litebank.service.application.queries.GetMoneyTransferProjectionQuery;
import com.litebank.service.application.queries.GetMoneyTransferProjectionQueryHandler;

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
        else if (classType == OpenAccountProjectionCommand.class) {
            commandHandler =  new OpenAccountProjectionCommandHandler(DI.getInstance().getAccountProjectionRepository());
        }
        else if (classType == DebitAccountProjectionCommand.class) {
            commandHandler =  new DebitAccountProjectionCommandHandler(DI.getInstance().getAccountProjectionRepository());
        }
        else if (classType == CreditAccountProjectionCommand.class) {
            commandHandler =  new CreditAccountProjectionCommandHandler(DI.getInstance().getAccountProjectionRepository());
        }
        else if (classType == CreateMoneyTransferProjectionCommand.class) {
            commandHandler =  new CreateMoneyTransferProjectionCommandHandler(DI.getInstance().getMoneyTransferProjectionRepository());
        }
        else if (classType == RegisterMoneyTransferFailCommand.class) {
            commandHandler =  new RegisterMoneyTransferFailCommandHandler(DI.getInstance().getMoneyTransferProjectionRepository());
        }
        else if (classType == RegisterMoneyTransferFinishedCommand.class) {
            commandHandler =  new RegisterMoneyTransferFinishedCommandHandler(DI.getInstance().getMoneyTransferProjectionRepository());
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
        QueryHandler queryHandler = null;

        if (classType == GetAccountProjectionQuery.class) {
            queryHandler =  new GetAccountProjectionQueryHandler(DI.getInstance().getAccountProjectionRepository());
        }
        else if (classType == GetMoneyTransferProjectionQuery.class) {
            queryHandler =  new GetMoneyTransferProjectionQueryHandler(DI.getInstance().getMoneyTransferProjectionRepository());
        }

        return queryHandler;
    }
}
