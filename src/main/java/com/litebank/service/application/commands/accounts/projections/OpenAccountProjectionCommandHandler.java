package com.litebank.service.application.commands.accounts.projections;

import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.service.domain.model.accounts.projections.AccountProjection;

public class OpenAccountProjectionCommandHandler implements CommandHandler<OpenAccountProjectionCommand> {

    private final AccountProjectionRepository accountRepository;

    public OpenAccountProjectionCommandHandler(AccountProjectionRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(OpenAccountProjectionCommand command) {
        var account = new AccountProjection(command.getAccountId(), command.getBalance());

        accountRepository.save(account);
    }
}




