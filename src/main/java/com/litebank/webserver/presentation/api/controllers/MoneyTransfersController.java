package com.litebank.webserver.presentation.api.controllers;

import com.litebank.webserver.application.commands.moneytransfers.MoneyTransferCommand;
import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferCreationResultDto;
import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferDto;
import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferStateDto;
import com.litebank.webserver.application.dtos.moneytransfers.TransferRequestDto;
import com.litebank.webserver.presentation.api.di.CommandQueriesFactory;
import io.javalin.http.Handler;

public class MoneyTransfersController {
    private CommandQueriesFactory commandQueriesFactory;

    public MoneyTransfersController(CommandQueriesFactory commandQueriesFactory) {
        this.commandQueriesFactory = commandQueriesFactory;
    }

    public Handler startTransfer = ctx -> {
        var transferRequest = ctx.bodyAsClass(TransferRequestDto.class);

        var handler = commandQueriesFactory.getCommandHandlerR(MoneyTransferCommand.class, MoneyTransferCreationResultDto.class);
        var transferCommand = new MoneyTransferCommand(transferRequest.getFromAccountId(), transferRequest.getToAccountId(), transferRequest.getAmount(), transferRequest.getCurrencyCode());

        var moneyTransferResult = handler.execute(transferCommand);

        if (!moneyTransferResult.hasError()) {
            var moneyTransferDto = new MoneyTransferDto(moneyTransferResult.getMoneyTransferId(), transferRequest.getFromAccountId(), transferRequest.getToAccountId(), transferRequest.getAmount(), transferRequest.getCurrencyCode(), MoneyTransferStateDto.SCHEDULED);

            ctx.status(202);
            ctx.json(moneyTransferDto);
        }
        else {
            ctx.status(400);
            ctx.json(moneyTransferResult.getErrorValidation());
        }
    };
}
