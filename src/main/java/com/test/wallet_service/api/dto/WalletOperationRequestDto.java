package com.test.wallet_service.api.dto;

import com.test.wallet_service.db.entity.enums.OperationType;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Getter
public class WalletOperationRequestDto {

    @NonNull
    private UUID walletId;

    @NonNull
    private OperationType operationType;

    @NonNull
    private BigDecimal amount;
}
