package com.litebank.webserver.application.commands.accounts.projections;

import com.litebank.webserver.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class OpenAccountProjectionCommandHandlerTest {
    @Test
    public void execute_everythingOk() {
        // Arrange
        var accountProjectionRepository = Mockito.mock(AccountProjectionRepository.class);

        var accountId = UUID.randomUUID();
        var openingAmount = new BigDecimal(10);

        var command = new OpenAccountProjectionCommand(accountId, openingAmount);
        var handler = new OpenAccountProjectionCommandHandler(accountProjectionRepository);

        // Act
        handler.execute(command);

        // Assert
        ArgumentCaptor<AccountProjection> captor = ArgumentCaptor.forClass(AccountProjection.class);
        verify(accountProjectionRepository).save(captor.capture());

        assertEquals(accountId, captor.getValue().getAccountId());
        assertEquals(openingAmount, captor.getValue().getBalance());
        assertTrue(captor.getValue().getTransactions().isEmpty());
    }
}