package com.litebank.webserver.application.commands.moneytransfers;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.webserver.domain.model.exceptions.MoneyTransferNotFoundException;

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
