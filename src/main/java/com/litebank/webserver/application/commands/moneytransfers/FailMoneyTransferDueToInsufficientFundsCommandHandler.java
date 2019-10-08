package com.litebank.webserver.application.commands.moneytransfers;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.webserver.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.webserver.domain.model.moneytransfers.MoneyTransfer;

public class FailMoneyTransferDueToInsufficientFundsCommandHandler implements CommandHandler<FailMoneyTransferDueToInsufficientFundsCommand> {
    private final MoneyTransferRepository moneyTransferRepository;

    public FailMoneyTransferDueToInsufficientFundsCommandHandler(MoneyTransferRepository moneyTransferRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public void execute(FailMoneyTransferDueToInsufficientFundsCommand command) {
        try {
            MoneyTransfer moneyTransfer = moneyTransferRepository.getById(command.getMoneyTransferId());

            moneyTransfer.debitedFailedWithInsufficientAmount();

            moneyTransferRepository.save(moneyTransfer);
        } catch (MoneyTransferNotFoundException e) { }
    }
}
