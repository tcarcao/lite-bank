package com.litebank.clients.java.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.litebank.clients.java.interfaces.AccountsClient;
import com.litebank.webserver.application.dtos.accounts.AccountOpenedDto;
import com.litebank.webserver.application.dtos.accounts.OpenAccountDto;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountsClientImpl implements AccountsClient {
    private final String basePath;
    private final ObjectMapper objectMapper;

    public AccountsClientImpl(String basePath) {
        this.basePath = basePath;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public CompletableFuture<AccountOpenedDto> openAccount(BigDecimal openingAmount, UUID customerId) {
        String uri = basePath + "/accounts";

        var openAccountRequest = new OpenAccountDto(openingAmount, customerId);

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(openAccountRequest)))
                    .build();

            var response = HttpClient.newBuilder()
                    .build()
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(r -> {
                        try {
                            return objectMapper.readValue(r, AccountOpenedDto.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });

            return response;

        } catch (JsonProcessingException e) {
        }

        return null;
    }

    @Override
    public CompletableFuture<Optional<AccountProjection>> getAccount(UUID accountId) {
        String uri = basePath + "/accounts/" + accountId;

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
                    try {
                        return Optional.of(objectMapper.readValue(r, AccountProjection.class));
                    } catch (JsonProcessingException e) {
                        Optional<AccountProjection> optionalAccountProjection = Optional.empty();
                        return optionalAccountProjection;
                    }
                });

        return response;
    }
}
