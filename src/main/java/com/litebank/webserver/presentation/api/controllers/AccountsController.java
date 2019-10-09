package com.litebank.webserver.presentation.api.controllers;

import com.litebank.webserver.application.commands.accounts.OpenAccountCommand;
import com.litebank.webserver.application.dtos.accounts.AccountOpenedDto;
import com.litebank.webserver.application.dtos.accounts.OpenAccountDto;
import com.litebank.webserver.application.queries.GetAccountProjectionQuery;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;
import com.litebank.webserver.presentation.api.di.CommandQueriesFactory;
import io.javalin.http.Handler;

import java.util.UUID;

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

        // TODO: missing map
        var account = handler.execute(getAccountProjectionQuery);

        if (account == null) {
            ctx.status(404);
        }
        else {
            ctx.json(account);
        }
    };

    private static AccountOpenedDto toDto(Account account) {
        return new AccountOpenedDto(account.getId(), account.getBalance(), account.getCustomerId());
    }
}
