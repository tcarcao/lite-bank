package com.litebank.webserver.presentation.api.controllers;

import com.litebank.webserver.application.commands.accounts.OpenAccountCommand;
import com.litebank.webserver.application.dtos.accounts.AccountDto;
import com.litebank.webserver.application.dtos.accounts.OpenAccountDto;
import com.litebank.webserver.application.dtos.accounts.TransactionDto;
import com.litebank.webserver.application.dtos.accounts.TransactionTypeDto;
import com.litebank.webserver.application.queries.GetAccountProjectionQuery;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;
import com.litebank.webserver.domain.model.accounts.projections.TransactionProjection;
import com.litebank.webserver.domain.model.accounts.projections.TransactionType;
import com.litebank.webserver.presentation.api.di.CommandQueriesFactory;
import io.javalin.http.Handler;

import java.util.UUID;
import java.util.stream.Collectors;

public class AccountsController {
    private CommandQueriesFactory commandQueriesFactory;

    public AccountsController(CommandQueriesFactory commandQueriesFactory) {
        this.commandQueriesFactory = commandQueriesFactory;
    }

    public Handler openAccount = ctx -> {
        var openAccountDto = ctx.bodyAsClass(OpenAccountDto.class);

        var handler = commandQueriesFactory.getCommandHandlerR(OpenAccountCommand.class, Account.class);
        var openAccountCommand = new OpenAccountCommand(openAccountDto.getOpeningAmount(), openAccountDto.getCustomerId());

        var account = handler.execute(openAccountCommand);

        ctx.status(201);
        ctx.json(toDto(account));
    };

    public Handler getAccount = ctx -> {
        UUID accountId = UUID.fromString(ctx.pathParam("accountId"));

        var handler = commandQueriesFactory.getQueryHandler(GetAccountProjectionQuery.class, AccountProjection.class);
        var getAccountProjectionQuery = new GetAccountProjectionQuery(accountId);

        var account = handler.execute(getAccountProjectionQuery);

        if (account == null) {
            ctx.status(404);
        }
        else {
            ctx.json(toDto(account));
        }
    };

    private static AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getBalance());
    }

    private static AccountDto toDto(AccountProjection account) {
        var transactionsDto = account.getTransactions().stream().map(AccountsController::toDto).collect(Collectors.toList());

        return new AccountDto(account.getAccountId(), account.getBalance(), transactionsDto);
    }

    private static TransactionDto toDto(TransactionProjection transactionProjection) {
        return new TransactionDto(transactionProjection.getAmount(), toDto(transactionProjection.getType()), transactionProjection.getTimestamp());
    }

    private static TransactionTypeDto toDto(TransactionType transactionType) {
        if (transactionType == TransactionType.CREDIT) {
            return TransactionTypeDto.CREDIT;
        }

        return TransactionTypeDto.DEBIT;
    }
}
