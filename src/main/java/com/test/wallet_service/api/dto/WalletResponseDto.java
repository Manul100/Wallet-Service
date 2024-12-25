package com.test.wallet_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class WalletResponseDto {

    private UUID walletId;

    private BigDecimal balance;

    private LocalDateTime completedAt;
}
