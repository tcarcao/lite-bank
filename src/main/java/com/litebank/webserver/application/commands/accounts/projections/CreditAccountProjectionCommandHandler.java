package com.litebank.webserver.application.commands.accounts.projections;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.AccountProjectionRepository;

public class CreditAccountProjectionCommandHandler implements CommandHandler<CreditAccountProjectionCommand> {

    private final AccountProjectionRepository accountRepository;

    public CreditAccountProjectionCommandHandler(AccountProjectionRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(CreditAccountProjectionCommand command) {
        var account = accountRepository.getByIdOrDefault(command.getAccountId());

        account.credit(command.getAmount(), command.getTimestamp());

        accountRepository.save(account);
    }
}
