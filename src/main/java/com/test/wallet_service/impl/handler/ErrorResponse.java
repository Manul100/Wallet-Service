package com.test.wallet_service.impl.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private final Integer code;
    private final String message;
    private final LocalDateTime timeStamp;

    public ErrorResponse(HttpStatus status, String message) {
        this.code = status != null ? status.value() : null;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }
}
