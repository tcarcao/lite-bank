package com.litebank.webserver.integrationtests;

import com.litebank.clients.java.implementations.AccountsClientImpl;
import com.litebank.clients.java.implementations.ApiErrorException;
import com.litebank.clients.java.implementations.MoneyTransfersClientImpl;
import com.litebank.clients.java.interfaces.AccountsClient;
import com.litebank.clients.java.interfaces.MoneyTransfersClient;
import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferStateDto;
import com.litebank.webserver.presentation.api.Application;
import com.litebank.webserver.testutils.RetryUtils;
import io.javalin.Javalin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class MoneyTransfersTest {

    private Javalin app;
    private MoneyTransfersClient moneyTransfersClient;
    private AccountsClient accountsClient;

    @Before
    public void setUp() throws Exception {
        app = Application.init(8000);
        var basePath = "http://localhost:" + app.port();
        moneyTransfersClient = new MoneyTransfersClientImpl(basePath);
        accountsClient = new AccountsClientImpl(basePath);
    }

    @After
    public void tearDown() throws Exception {
        app.stop();
    }

    @Test
    public void createMoneyTransfer_everythingOk() throws InterruptedException, ExecutionException {
        // Arrange
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";

        var fromAccount = accountsClient.openAccount(new BigDecimal(100), UUID.randomUUID()).get();
        var toAccount = accountsClient.openAccount(new BigDecimal(100), UUID.randomUUID()).get();

        RetryUtils.retry(() -> accountsClient.getAccount(fromAccount.getAccountId()).get().orElse(null), 4);
        RetryUtils.retry(() -> accountsClient.getAccount(toAccount.getAccountId()).get().orElse(null), 4);

        // Act
        var moneyTransfer = moneyTransfersClient.createMoneyTransfer(fromAccount.getAccountId(), toAccount.getAccountId(), amount, currencyCode).get();

        // Assert
        assertNotNull(moneyTransfer);
        assertEquals(MoneyTransferStateDto.SCHEDULED, moneyTransfer.getState());

        var moneyTransferId = moneyTransfer.getMoneyTransferId();

        moneyTransfer = RetryUtils.retry(() -> {
            var tempTransfer = moneyTransfersClient.getMoneyTransfer(moneyTransferId)
                    .get()
                    .orElse(null);

            if (tempTransfer != null && tempTransfer.getState() != MoneyTransferStateDto.FINISHED) {
                return null;
            }

            return tempTransfer;
        }, 10);

        assertNotNull(moneyTransfer);
    }

    @Test
    public void createMoneyTransfer_insufficientFunds_transferShouldFail() throws InterruptedException, ExecutionException {
        // Arrange
        var amount = new BigDecimal(101);
        var currencyCode = "EUR";

        var fromAccount = accountsClient.openAccount(new BigDecimal(100), UUID.randomUUID()).get();
        var toAccount = accountsClient.openAccount(new BigDecimal(100), UUID.randomUUID()).get();

        RetryUtils.retry(() -> accountsClient.getAccount(fromAccount.getAccountId()).get().orElse(null), 4);
        RetryUtils.retry(() -> accountsClient.getAccount(toAccount.getAccountId()).get().orElse(null), 4);

        // Act
        var moneyTransfer = moneyTransfersClient.createMoneyTransfer(fromAccount.getAccountId(), toAccount.getAccountId(), amount, currencyCode).get();

        // Assert
        assertNotNull(moneyTransfer);
        assertEquals(MoneyTransferStateDto.SCHEDULED, moneyTransfer.getState());

        var moneyTransferId = moneyTransfer.getMoneyTransferId();

        moneyTransfer = RetryUtils.retry(() -> {
            var tempTransfer = moneyTransfersClient.getMoneyTransfer(moneyTransferId)
                    .get()
                    .orElse(null);

            if (tempTransfer != null && tempTransfer.getState() != MoneyTransferStateDto.FAILED) {
                return null;
            }

            return tempTransfer;
        }, 10);

        assertNotNull(moneyTransfer);
        assertNotNull(moneyTransfer.getError());
        assertEquals("InsufficientFunds", moneyTransfer.getError().getCode());
    }

    @Test
    public void createMoneyTransfer_invalidAmount_transferShouldFail() throws InterruptedException, ExecutionException {
        // Arrange
        var amount = new BigDecimal(0);
        var currencyCode = "EUR";

        var fromAccount = accountsClient.openAccount(new BigDecimal(100), UUID.randomUUID()).get();
        var toAccount = accountsClient.openAccount(new BigDecimal(100), UUID.randomUUID()).get();

        RetryUtils.retry(() -> accountsClient.getAccount(fromAccount.getAccountId()).get().orElse(null), 4);
        RetryUtils.retry(() -> accountsClient.getAccount(toAccount.getAccountId()).get().orElse(null), 4);

        // Act
        try {
            var moneyTransfer = moneyTransfersClient.createMoneyTransfer(fromAccount.getAccountId(), toAccount.getAccountId(), amount, currencyCode).get();
            fail();
        } catch (Exception ex) {
            // Assert
            try {
                throw ex.getCause();
            }
            catch (ApiErrorException e) {
                assertEquals(400, e.getStatusCode());
                assertEquals("InvalidAmount", e.getMessage());
            } catch (Throwable throwable) {
                fail();
            }
        }
    }

    @Test
    public void createMoneyTransfer_originAndDestinationEqual_transferShouldFail() throws InterruptedException, ExecutionException {
        // Arrange
        var amount = new BigDecimal(101);
        var currencyCode = "EUR";

        var fromAccount = accountsClient.openAccount(new BigDecimal(100), UUID.randomUUID()).get();

        RetryUtils.retry(() -> accountsClient.getAccount(fromAccount.getAccountId()).get().orElse(null), 4);

        // Act
        try {
            var moneyTransfer = moneyTransfersClient.createMoneyTransfer(fromAccount.getAccountId(), fromAccount.getAccountId(), amount, currencyCode).get();
            fail();
        } catch (Exception ex) {
            // Assert
            try {
                throw ex.getCause();
            }
            catch (ApiErrorException e) {
                assertEquals(400, e.getStatusCode());
                assertEquals("OriginEqualToDestination", e.getMessage());
            } catch (Throwable throwable) {
                fail();
            }
        }
    }
}
