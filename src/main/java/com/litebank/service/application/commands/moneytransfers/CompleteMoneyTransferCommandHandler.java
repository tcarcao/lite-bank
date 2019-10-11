package com.litebank.service.application.commands.moneytransfers;

import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.service.domain.model.exceptions.MoneyTransferNotFoundException;

public class CompleteMoneyTransferCommandHandler implements CommandHandler<CompleteMoneyTransferCommand> {

    private final MoneyTransferRepository moneyTransferRepository;

    public CompleteMoneyTransferCommandHandler(MoneyTransferRepository moneyTransferRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public void execute(CompleteMoneyTransferCommand command) {
        try {
            var moneyTransfer = moneyTransferRepository.getById(command.getMoneyTransferId());

            moneyTransfer.finishedWithSuccess();

            moneyTransferRepository.save(moneyTransfer);
        } catch (MoneyTransferNotFoundException e) { }
    }
}
