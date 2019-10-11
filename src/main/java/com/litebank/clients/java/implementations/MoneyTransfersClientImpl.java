package com.litebank.clients.java.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.litebank.clients.java.interfaces.MoneyTransfersClient;
import com.litebank.service.application.dtos.moneytransfers.MoneyTransferDto;
import com.litebank.service.application.dtos.moneytransfers.MoneyTransferValidationDto;
import com.litebank.service.application.dtos.moneytransfers.TransferRequestDto;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MoneyTransfersClientImpl implements MoneyTransfersClient {
    private final String basePath;
    private final ObjectMapper objectMapper;

    public MoneyTransfersClientImpl(String basePath) {
        this.basePath = basePath;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public CompletableFuture<MoneyTransferDto> createMoneyTransfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode) {
        String uri = basePath + "/transfers";

        var moneyTransferRequest = new TransferRequestDto(fromAccountId, toAccountId, amount, currencyCode);

        HttpRequest httpRequest = null;
        try {
            httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(moneyTransferRequest)))
                    .build();

            return HttpClient.newBuilder()
                    .build()
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(r -> {
                        if (r.statusCode() >= 400) {
                            try {
                                var validation = objectMapper.readValue(r.body(), MoneyTransferValidationDto.class);
                                throw new ApiErrorException(r.statusCode(), validation.getValidation());
                            } catch (JsonProcessingException e) {
                                throw new ApiErrorException(r.statusCode(), "Error");
                            }
                        }

                        return r;
                    })
                    .thenApply(HttpResponse::body)
                    .thenApply(r -> {
                        try {
                            return objectMapper.readValue(r, MoneyTransferDto.class);
                        } catch (JsonProcessingException e) { throw new RuntimeException(e); }
                    });
        } catch (JsonProcessingException e) {

        }

        return null;
    }

    @Override
    public CompletableFuture<Optional<MoneyTransferDto>> getMoneyTransfer(UUID moneyTransfer) {
        String uri = basePath + "/transfers/" + moneyTransfer;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = HttpClient.newBuilder()
                .build()
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(r -> {
                    Optional<MoneyTransferDto> moneyTransferDto;

                    try {
                        moneyTransferDto = Optional.of(objectMapper.readValue(r, MoneyTransferDto.class));
                    } catch (JsonProcessingException e) {
                        moneyTransferDto = Optional.empty();
                    }

                    return moneyTransferDto;
                });

        return response;
    }
}
