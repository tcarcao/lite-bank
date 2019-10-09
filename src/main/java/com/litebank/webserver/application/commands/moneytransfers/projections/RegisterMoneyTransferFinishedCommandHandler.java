package com.litebank.webserver.application.commands.moneytransfers.projections;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferProjectionRepository;

public class RegisterMoneyTransferFinishedCommandHandler implements CommandHandler<RegisterMoneyTransferFinishedCommand> {
    private final MoneyTransferProjectionRepository moneyTransferProjectionRepository;

    public RegisterMoneyTransferFinishedCommandHandler(MoneyTransferProjectionRepository moneyTransferProjectionRepository) {
        this.moneyTransferProjectionRepository = moneyTransferProjectionRepository;
    }

    @Override
    public void execute(RegisterMoneyTransferFinishedCommand command) {
        var moneyProjection = moneyTransferProjectionRepository.getByIdOrDefault(command.getMoneyTransferId());

        moneyProjection.finish();

        moneyTransferProjectionRepository.save(moneyProjection);
    }
}
