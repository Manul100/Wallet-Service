package com.test.wallet_service;

import com.test.wallet_service.api.dto.WalletOperationRequestDto;
import com.test.wallet_service.api.dto.WalletResponseDto;
import com.test.wallet_service.db.entity.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestGenerator {

    private static final UUID ID = UUID.randomUUID();
    public static final BigDecimal BALANCE = new BigDecimal(200);
    public static final BigDecimal AMOUNT = new BigDecimal(100);

    public static WalletOperationRequestDto generateDepositWalletOperationDto() {
        return new WalletOperationRequestDto(ID, OperationType.DEPOSIT, AMOUNT);
    }

    public static WalletResponseDto generateWalletResponseDto() {
        return new WalletResponseDto(ID, BALANCE, LocalDateTime.now());
    }
}
