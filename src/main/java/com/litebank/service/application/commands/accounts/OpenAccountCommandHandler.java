package com.litebank.service.application.commands.accounts;

import com.litebank.service.application.interfaces.cqrs.CommandHandlerR;
import com.litebank.service.application.interfaces.repositories.AccountRepository;
import com.litebank.service.domain.model.accounts.Account;

import java.util.UUID;

public class OpenAccountCommandHandler implements CommandHandlerR<OpenAccountCommand, Account> {
    private final AccountRepository accountRepository;

    public OpenAccountCommandHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(OpenAccountCommand command) {
        var account = new Account(UUID.randomUUID(), command.getCustomerId(), command.getOpeningAmount());

        accountRepository.save(account);

        return account;
    }
}
