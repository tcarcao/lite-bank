package com.litebank.webserver.presentation.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litebank.webserver.application.commands.moneytransfers.MoneyTransferCommand;
import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferCreationResultDto;
import com.litebank.webserver.application.dtos.moneytransfers.MoneyTransferValidationDto;
import com.litebank.webserver.application.dtos.moneytransfers.TransferRequestDto;
import com.litebank.webserver.application.interfaces.cqrs.CommandHandlerR;
import com.litebank.webserver.application.interfaces.cqrs.QueryHandler;
import com.litebank.webserver.application.queries.GetMoneyTransferProjectionQuery;
import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferProjection;
import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferStateProjection;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MoneyTransfersControllerTest {
    @Captor
    private ArgumentCaptor<Class> queryHandlerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Class> commandHandlerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Class> handlerResultArgumentCaptor;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startTransfer_accountsExists_transferIsStarted() throws Exception {
        // Arrange
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandlerR.class);
        var controller = new MoneyTransfersController(commandQueriesFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        var req = Mockito.mock(HttpServletRequest.class);
        var res = Mockito.mock(HttpServletResponse.class);

        Context context = ContextUtil.init(req, res);

        var moneyTransferId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";
        var transferRequest = new TransferRequestDto(fromAccountId, toAccountId, amount, currencyCode);

        var moneyTransferResult = new MoneyTransferCreationResultDto(moneyTransferId);

        when(commandQueriesFactory.getCommandHandlerR(any(), any())).thenReturn(genericCommandHandler);
        when(genericCommandHandler.execute(any(MoneyTransferCommand.class))).thenReturn(moneyTransferResult);

        var inputStream = InputStreamUtils.getServletInputStream(objectMapper, transferRequest);
        when(req.getInputStream()).thenReturn(inputStream);

        // Act
        controller.startTransfer.handle(context);

        // Assert
        verify(commandQueriesFactory).getCommandHandlerR(commandHandlerArgumentCaptor.capture(), handlerResultArgumentCaptor.capture());

        assertEquals(MoneyTransferCommand.class, commandHandlerArgumentCaptor.getValue());
        verify(genericCommandHandler, times(1)).execute(any(MoneyTransferCommand.class));

        assertNotNull(context.resultStream());
        String result = new BufferedReader(new InputStreamReader(context.resultStream()))
                .lines().collect(Collectors.joining("\n"));

        assertNotNull(result);
        verify(res).setStatus(202);
    }

    @Test
    public void startTransfer_moneyTransferHasError_transferFails() throws Exception {
        // Arrange
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericCommandHandler = Mockito.mock(CommandHandlerR.class);
        var controller = new MoneyTransfersController(commandQueriesFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        var req = Mockito.mock(HttpServletRequest.class);
        var res = Mockito.mock(HttpServletResponse.class);

        Context context = ContextUtil.init(req, res);

        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";
        var transferRequest = new TransferRequestDto(fromAccountId, toAccountId, amount, currencyCode);

        var moneyValidation = new MoneyTransferValidationDto("error", "error");

        var moneyTransferResult = new MoneyTransferCreationResultDto(moneyValidation);

        when(commandQueriesFactory.getCommandHandlerR(any(), any())).thenReturn(genericCommandHandler);
        when(genericCommandHandler.execute(any(MoneyTransferCommand.class))).thenReturn(moneyTransferResult);

        var inputStream = InputStreamUtils.getServletInputStream(objectMapper, transferRequest);
        when(req.getInputStream()).thenReturn(inputStream);

        // Act
        controller.startTransfer.handle(context);

        // Assert
        verify(commandQueriesFactory).getCommandHandlerR(commandHandlerArgumentCaptor.capture(), handlerResultArgumentCaptor.capture());

        assertEquals(MoneyTransferCommand.class, commandHandlerArgumentCaptor.getValue());
        verify(genericCommandHandler, times(1)).execute(any(MoneyTransferCommand.class));

        assertNotNull(context.resultStream());
        String result = new BufferedReader(new InputStreamReader(context.resultStream()))
                .lines().collect(Collectors.joining("\n"));

        assertNotNull(result);
        verify(res).setStatus(400);
    }

    @Test
    public void getMoneyTransfer_moneyTransferExists_everythingOk() throws Exception {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericQueryHandler = Mockito.mock(QueryHandler.class);
        var controller = new MoneyTransfersController(commandQueriesFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        var req = Mockito.mock(HttpServletRequest.class);
        var res = Mockito.mock(HttpServletResponse.class);

        var pathParams = new TreeMap<String, String>();
        pathParams.put("moneyTransferId", moneyTransferId.toString());

        Context context = ContextUtil.init(req, res, "", pathParams);

        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";
        var moneyTransferProjection = new MoneyTransferProjection(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode, MoneyTransferStateProjection.SCHEDULED);

        when(commandQueriesFactory.getQueryHandler(any(), any())).thenReturn(genericQueryHandler);
        when(genericQueryHandler.execute(any(GetMoneyTransferProjectionQuery.class))).thenReturn(moneyTransferProjection);

        // Act
        controller.getMoneyTransfer.handle(context);

        // Assert
        verify(commandQueriesFactory).getQueryHandler(queryHandlerArgumentCaptor.capture(), handlerResultArgumentCaptor.capture());

        assertEquals(GetMoneyTransferProjectionQuery.class, queryHandlerArgumentCaptor.getValue());
        verify(genericQueryHandler, times(1)).execute(any(GetMoneyTransferProjectionQuery.class));

        assertNotNull(context.resultStream());
        String result = new BufferedReader(new InputStreamReader(context.resultStream()))
                .lines().collect(Collectors.joining("\n"));

        assertNotNull(result);
    }

    @Test
    public void getMoneyTransfer_moneyTransferDoesNotExist_returnsNotFound() throws Exception {
        // Arrange
        var moneyTransferId = UUID.randomUUID();
        var commandQueriesFactory = Mockito.mock(CommandQueriesFactory.class);
        var genericQueryHandler = Mockito.mock(QueryHandler.class);
        var controller = new MoneyTransfersController(commandQueriesFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        var req = Mockito.mock(HttpServletRequest.class);
        var res = Mockito.mock(HttpServletResponse.class);

        var pathParams = new TreeMap<String, String>();
        pathParams.put("moneyTransferId", moneyTransferId.toString());

        Context context = ContextUtil.init(req, res, "", pathParams);

        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var amount = new BigDecimal(10);
        var currencyCode = "EUR";
        MoneyTransferProjection moneyTransferProjection = null;

        when(commandQueriesFactory.getQueryHandler(any(), any())).thenReturn(genericQueryHandler);
        when(genericQueryHandler.execute(any(GetMoneyTransferProjectionQuery.class))).thenReturn(moneyTransferProjection);

        // Act
        controller.getMoneyTransfer.handle(context);

        // Assert
        verify(commandQueriesFactory).getQueryHandler(queryHandlerArgumentCaptor.capture(), handlerResultArgumentCaptor.capture());

        assertEquals(GetMoneyTransferProjectionQuery.class, queryHandlerArgumentCaptor.getValue());
        verify(genericQueryHandler, times(1)).execute(any(GetMoneyTransferProjectionQuery.class));

        assertNull(context.resultStream());
        verify(res).setStatus(404);
    }
}