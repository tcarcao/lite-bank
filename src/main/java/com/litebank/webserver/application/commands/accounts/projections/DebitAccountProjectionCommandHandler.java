package com.litebank.webserver.application.commands.accounts.projections;

import com.litebank.webserver.application.interfaces.cqrs.CommandHandler;
import com.litebank.webserver.application.interfaces.repositories.AccountProjectionRepository;

public class DebitAccountProjectionCommandHandler implements CommandHandler<DebitAccountProjectionCommand> {

    private final AccountProjectionRepository accountRepository;

    public DebitAccountProjectionCommandHandler(AccountProjectionRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(DebitAccountProjectionCommand command) {
        var account = accountRepository.getByIdOrDefault(command.getAccountId());

        account.debit(command.getAmount(), command.getTimestamp());

        accountRepository.save(account);
    }
}
