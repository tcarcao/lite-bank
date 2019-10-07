package com.litebank.webserver.application.commands.moneytransfers;

import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferCreationResultDto;
import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferValidationDto;
import com.litebank.webserver.application.interfaces.cqrs.CommandHandlerR;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.webserver.application.interfaces.repositories.ReadOnlyAccountRepository;
import com.litebank.webserver.domain.model.exceptions.AccountNotFoundException;
import com.litebank.webserver.domain.model.moneytransfers.MoneyTransfer;

import java.util.UUID;

public class MoneyTransferCommandHandler implements CommandHandlerR<MoneyTransferCommand, MoneyTransferCreationResultDto> {

    private final ReadOnlyAccountRepository accountRepository;
    private final MoneyTransferRepository moneyTransferRepository;

    public MoneyTransferCommandHandler(ReadOnlyAccountRepository accountRepository, MoneyTransferRepository moneyTransferRepository) {
        this.accountRepository = accountRepository;
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public MoneyTransferCreationResultDto execute(MoneyTransferCommand moneyTransferCommand) {
        try {
            accountRepository.getById(moneyTransferCommand.getFromAccountId());
            accountRepository.getById(moneyTransferCommand.getToAccountId());

            MoneyTransfer transfer = new MoneyTransfer(UUID.randomUUID(), moneyTransferCommand.getFromAccountId(), moneyTransferCommand.getToAccountId(), moneyTransferCommand.getAmount(), moneyTransferCommand.getCurrencyCode());

            var moneyTransferId = moneyTransferRepository.save(transfer);

            return new MoneyTransferCreationResultDto(moneyTransferId);
        } catch (AccountNotFoundException e) {
            return new MoneyTransferCreationResultDto(new MoneyTransferValidationDto("AccountNotFound", e.getMessage()));
        }
    }
}
