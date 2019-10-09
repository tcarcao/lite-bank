package com.litebank.webserver.presentation.api.controllers;

import com.litebank.webserver.application.commands.accounts.OpenAccountCommand;
import com.litebank.webserver.application.dtos.accounts.AccountOpenedDto;
import com.litebank.webserver.application.dtos.accounts.OpenAccountDto;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.presentation.api.di.CommandQueriesFactory;
import io.javalin.http.Handler;

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

    private static AccountOpenedDto toDto(Account account) {
        return new AccountOpenedDto(account.getId(), account.getBalance(), account.getCustomerId());
    }
}
