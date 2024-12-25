package com.test.wallet_service.impl.handler;

import com.test.wallet_service.impl.exception.NotFoundException;
import com.test.wallet_service.impl.exception.OperationValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse allHandler(Throwable e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse notFoundHandler(NotFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OperationValidationException.class)
    public ErrorResponse operationValidationHandler(OperationValidationException e) {
        return new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }
}
