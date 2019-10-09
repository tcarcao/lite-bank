package com.litebank.clients.java.implementations;

public class ApiErrorException extends RuntimeException {
    private final int statusCode;
    private final String message;

    public ApiErrorException(int statusCode, String message) {
        super(message);

        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
