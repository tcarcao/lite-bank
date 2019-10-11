package com.litebank.service.presentation.api.controllers;

import com.litebank.service.application.commands.moneytransfers.MoneyTransferCommand;
import com.litebank.service.application.dtos.moneytransfers.*;
import com.litebank.service.application.queries.GetMoneyTransferProjectionQuery;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferErrorProjection;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferProjection;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferStateProjection;
import com.litebank.service.presentation.api.di.CommandQueriesFactory;
import io.javalin.http.Handler;

import java.util.UUID;

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

    public Handler getMoneyTransfer = ctx -> {
        UUID moneyTransferId = UUID.fromString(ctx.pathParam("moneyTransferId"));

        var handler = commandQueriesFactory.getQueryHandler(GetMoneyTransferProjectionQuery.class, MoneyTransferProjection.class);
        var query = new GetMoneyTransferProjectionQuery(moneyTransferId);

        var moneyTransfer = handler.execute(query);

        if (moneyTransfer == null) {
            ctx.status(404);
        }
        else {
            ctx.json(toDto(moneyTransfer));
        }
    };

    private static MoneyTransferDto toDto(MoneyTransferProjection moneyTransfer) {
        return new MoneyTransferDto(moneyTransfer.getMoneyTransferId(), moneyTransfer.getFromAccountId(), moneyTransfer.getToAccountId(), moneyTransfer.getAmount(), moneyTransfer.getCurrencyCode(), toDto(moneyTransfer.getState()), toDto(moneyTransfer.getError()));
    }

    private static MoneyTransferStateDto toDto(MoneyTransferStateProjection state) {
        switch (state) {
            case SCHEDULED:
                return MoneyTransferStateDto.SCHEDULED;
            case FAILED:
                return MoneyTransferStateDto.FAILED;
            default:
                return MoneyTransferStateDto.FINISHED;
        }
    }

    private static MoneyTransferErrorDto toDto(MoneyTransferErrorProjection error)
    {
        if (error == null) {
            return null;
        }

        return new MoneyTransferErrorDto(error.getCode(), error.getMessage());
    }
}
