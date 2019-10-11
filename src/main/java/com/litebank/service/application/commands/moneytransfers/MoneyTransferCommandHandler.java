package com.litebank.service.application.commands.moneytransfers;

import com.litebank.service.application.dtos.moneytransfers.MoneyTransferCreationResultDto;
import com.litebank.service.application.dtos.moneytransfers.MoneyTransferValidationDto;
import com.litebank.service.application.interfaces.cqrs.CommandHandlerR;
import com.litebank.service.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.service.application.interfaces.repositories.ReadOnlyAccountRepository;
import com.litebank.service.domain.model.exceptions.AccountNotFoundException;
import com.litebank.service.domain.model.exceptions.InvalidAmountMoneyTransferException;
import com.litebank.service.domain.model.exceptions.MoneyTransferOriginEqualToDestinationException;
import com.litebank.service.domain.model.moneytransfers.MoneyTransfer;

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

            var moneyTransferId = moneyTransferRepository.save(transfer).join();

            return new MoneyTransferCreationResultDto(moneyTransferId);
        } catch (AccountNotFoundException e) {
            return new MoneyTransferCreationResultDto(new MoneyTransferValidationDto("AccountNotFound", e.getMessage()));
        } catch (MoneyTransferOriginEqualToDestinationException e) {
            return new MoneyTransferCreationResultDto(new MoneyTransferValidationDto("OriginEqualToDestination", e.getMessage()));
        } catch (InvalidAmountMoneyTransferException e) {
            return new MoneyTransferCreationResultDto(new MoneyTransferValidationDto("InvalidAmount", e.getMessage()));
        }
    }
}
