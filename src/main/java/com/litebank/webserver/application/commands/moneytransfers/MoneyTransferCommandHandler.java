package com.litebank.webserver.application.commands.moneytransfers;

import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferCreationResultDto;
import com.litebank.webserver.application.interfaces.cqrs.CommandHandlerR;

public class MoneyTransferCommandHandler implements CommandHandlerR<MoneyTransferCommand, MoneyTransferCreationResultDto> {
    @Override
    public MoneyTransferCreationResultDto execute(MoneyTransferCommand command) {
        throw new RuntimeException();
    }
}
