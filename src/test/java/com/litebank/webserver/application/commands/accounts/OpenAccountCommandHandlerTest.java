package com.litebank.webserver.application.commands.accounts;

import com.litebank.webserver.application.interfaces.repositories.AccountRepository;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.domain.model.accounts.events.AccountOpenedEvent;
import com.litebank.webserver.domain.model.exceptions.AccountNotFoundException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

public class OpenAccountCommandHandlerTest {
    @Test
    public void execute_everythingOk() throws AccountNotFoundException {
        // Arrange
        var accountRepository = Mockito.mock(AccountRepository.class);

        var openingAmount = new BigDecimal(10);

        var command = new OpenAccountCommand(openingAmount, UUID.randomUUID());
        var handler = new OpenAccountCommandHandler(accountRepository);

        // Act
        var account = handler.execute(command);

        // Assert
        assertNotNull(account);
        assertNotNull(account.getId());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertEquals(1, captor.getValue().getNewEvents().size());
        assertEquals(AccountOpenedEvent.class, captor.getValue().getNewerEvent().get().getClass());

        var accountOpenedEvent = (AccountOpenedEvent) captor.getValue().getNewerEvent().get();
        assertEquals(openingAmount, accountOpenedEvent.getBalance());
    }
}
