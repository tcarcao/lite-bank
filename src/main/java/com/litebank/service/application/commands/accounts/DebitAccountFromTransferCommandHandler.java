package com.litebank.service.application.commands.accounts;

import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.application.interfaces.repositories.AccountRepository;
import com.litebank.service.domain.model.exceptions.AccountNotFoundException;

public class DebitAccountFromTransferCommandHandler implements CommandHandler<DebitAccountFromTransferCommand> {

    private final AccountRepository accountRepository;

    public DebitAccountFromTransferCommandHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(DebitAccountFromTransferCommand command) {
        try {
            var accountToDebit = accountRepository.getById(command.getAccountToDebitId());

            accountToDebit.tryToDebitAmountFromMoneyTransfer(command.getAmount(), command.getMoneyTransferId());

            accountRepository.save(accountToDebit);
        } catch (AccountNotFoundException e) { }
    }
}
