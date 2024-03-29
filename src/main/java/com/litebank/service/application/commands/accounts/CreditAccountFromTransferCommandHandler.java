package com.litebank.service.application.commands.accounts;

import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.application.interfaces.repositories.AccountRepository;
import com.litebank.service.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.service.domain.model.accounts.Account;
import com.litebank.service.domain.model.exceptions.AccountNotFoundException;
import com.litebank.service.domain.model.exceptions.MoneyTransferNotFoundException;

public class CreditAccountFromTransferCommandHandler implements CommandHandler<CreditAccountFromTransferCommand> {

    private final MoneyTransferRepository moneyTransferRepository;
    private final AccountRepository accountRepository;

    public CreditAccountFromTransferCommandHandler(MoneyTransferRepository moneyTransferRepository, AccountRepository accountRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
        this.accountRepository = accountRepository;
    }

    public void execute(CreditAccountFromTransferCommand command) {
        try {
            var moneyTransfer = moneyTransferRepository.getById(command.getMoneyTransferId());

            Account accountToCredit = accountRepository.getById(moneyTransfer.getToAccountId());

            moneyTransfer.debitedWithSuccess();
            accountToCredit.creditAmountFromMoneyTransfer(command.getAmount(), command.getMoneyTransferId());

            moneyTransferRepository.save(moneyTransfer).join();
            accountRepository.save(accountToCredit).join();
        } catch (AccountNotFoundException | MoneyTransferNotFoundException e) { }
    }
}
