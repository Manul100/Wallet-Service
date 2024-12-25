package com.test.wallet_service.api.service;

import com.test.wallet_service.api.dto.WalletOperationRequestDto;
import com.test.wallet_service.api.dto.WalletResponseDto;
import com.test.wallet_service.db.entity.Wallet;

import java.util.List;
import java.util.UUID;

public interface WalletService {

    WalletResponseDto performOperation(WalletOperationRequestDto request);

    WalletResponseDto getWalletDetails(UUID walletId);

    List<Wallet> findAll();
}
