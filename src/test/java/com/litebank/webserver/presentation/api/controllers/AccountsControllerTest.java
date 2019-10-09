package com.litebank.webserver.presentation.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litebank.webserver.application.commands.accounts.OpenAccountCommand;
import com.litebank.webserver.application.dtos.accounts.OpenAccountDto;
import com.litebank.webserver.application.interfaces.cqrs.CommandHandlerR;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.presentation.api.di.CommandQueriesFactory;
import com.litebank.webserver.testutils.InputStreamUtils;
import io.javalin.http.Context;
import io.javalin.http.util.ContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountsControllerTest {
    @Captor
    private ArgumentCaptor<Class> commandHandlerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Class> handlerResultArgumentCaptor;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void openAccount_everythingOk() throws Exception {
        // Arrange
        var accountId = UUID.randomUUID();
        var balance = new BigDecimal(10);
        var customerId = UUID.randomUUID();

        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandlerR.class);
        var controller = new AccountsController(commandQueriesFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        var req = Mockito.mock(HttpServletRequest.class);
        var res = Mockito.mock(HttpServletResponse.class);

        Context context = ContextUtil.init(req, res, "", new TreeMap<>());

        OpenAccountDto accountOpen = new OpenAccountDto(balance, customerId);
        var inputStream = InputStreamUtils.getServletInputStream(objectMapper, accountOpen);
        Account account = new Account(accountId);

        when(commandQueriesFactory.getCommandHandlerR(any(), any())).thenReturn(genericCommandHandler);
        when(genericCommandHandler.execute(any(OpenAccountCommand.class))).thenReturn(account);
        when(req.getInputStream()).thenReturn(inputStream);

        // Act
        controller.openAccount.handle(context);

        // Assert
        verify(commandQueriesFactory).getCommandHandlerR(commandHandlerArgumentCaptor.capture(), handlerResultArgumentCaptor.capture());

        assertEquals(OpenAccountCommand.class, commandHandlerArgumentCaptor.getValue());
        verify(genericCommandHandler, times(1)).execute(any(OpenAccountCommand.class));

        assertNotNull(context.resultStream());
        String result = new BufferedReader(new InputStreamReader(context.resultStream()))
                .lines().collect(Collectors.joining("\n"));

        assertNotNull(result);
        verify(res).setStatus(201);
    }
}