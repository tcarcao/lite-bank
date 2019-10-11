package com.litebank.service.application.commands.moneytransfers;

import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.service.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.service.domain.model.moneytransfers.MoneyTransfer;

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
