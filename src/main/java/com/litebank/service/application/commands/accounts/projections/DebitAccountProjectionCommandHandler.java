package com.litebank.service.application.commands.accounts.projections;

import com.litebank.service.application.interfaces.cqrs.CommandHandler;
import com.litebank.service.application.interfaces.repositories.AccountProjectionRepository;

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
