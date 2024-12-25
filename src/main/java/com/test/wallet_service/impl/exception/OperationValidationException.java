package com.test.wallet_service.impl.exception;

public class OperationValidationException extends RuntimeException{
    public OperationValidationException(String message) {
        super(message);
    }
}
