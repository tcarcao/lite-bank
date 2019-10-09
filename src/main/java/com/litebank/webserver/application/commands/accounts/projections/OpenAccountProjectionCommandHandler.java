package com.litebank.webserver.application.commands.accounts.projections;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;

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




