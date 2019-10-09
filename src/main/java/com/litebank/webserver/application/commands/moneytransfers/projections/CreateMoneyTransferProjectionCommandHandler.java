package com.litebank.webserver.application.commands.moneytransfers.projections;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferProjectionRepository;
import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferProjection;
import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferStateProjection;

public class CreateMoneyTransferProjectionCommandHandler implements CommandHandler<CreateMoneyTransferProjectionCommand> {
    private final MoneyTransferProjectionRepository moneyTransferProjectionRepository;

    public CreateMoneyTransferProjectionCommandHandler(MoneyTransferProjectionRepository moneyTransferProjectionRepository) {
        this.moneyTransferProjectionRepository = moneyTransferProjectionRepository;
    }

    @Override
    public void execute(CreateMoneyTransferProjectionCommand command) {
        var moneyTransfer = new MoneyTransferProjection(command.getMoneyTransferId(), command.getFromAccountId(), command.getToAccountId(), command.getAmount(), command.getCurrencyCode(), MoneyTransferStateProjection.SCHEDULED);

        moneyTransferProjectionRepository.save(moneyTransfer);
    }
}
