package com.litebank.webserver.application.commands.moneytransfers.projections;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferProjectionRepository;

public class RegisterMoneyTransferFailCommandHandler implements CommandHandler<RegisterMoneyTransferFailCommand> {
    private final MoneyTransferProjectionRepository moneyTransferProjectionRepository;

    public RegisterMoneyTransferFailCommandHandler(MoneyTransferProjectionRepository moneyTransferProjectionRepository) {
        this.moneyTransferProjectionRepository = moneyTransferProjectionRepository;
    }

    @Override
    public void execute(RegisterMoneyTransferFailCommand command) {
        var moneyProjection = moneyTransferProjectionRepository.getByIdOrDefault(command.getMoneyTransferId());

        moneyProjection.fail(command.getReason());

        moneyTransferProjectionRepository.save(moneyProjection);
    }
}
