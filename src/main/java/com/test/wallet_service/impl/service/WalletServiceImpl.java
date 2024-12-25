package com.test.wallet_service.impl.service;

import com.test.wallet_service.api.dto.WalletOperationRequestDto;
import com.test.wallet_service.api.dto.WalletResponseDto;
import com.test.wallet_service.api.service.WalletService;
import com.test.wallet_service.db.entity.Wallet;
import com.test.wallet_service.db.entity.enums.OperationType;
import com.test.wallet_service.db.repository.WalletRepository;
import com.test.wallet_service.impl.exception.NotFoundException;
import com.test.wallet_service.impl.exception.OperationValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public WalletResponseDto performOperation(WalletOperationRequestDto request) {
        Wallet wallet = findWalletById(request.getWalletId());

        if (request.getOperationType() == OperationType.WITHDRAW &&
                wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new OperationValidationException("Insufficient balance");
        }

        BigDecimal updatedBalance = request.getOperationType() == OperationType.DEPOSIT
                ? wallet.getBalance().add(request.getAmount())
                : wallet.getBalance().subtract(request.getAmount());

        wallet.setBalance(updatedBalance);
        walletRepository.save(wallet);
        return new WalletResponseDto(wallet.getWalletId(), wallet.getBalance(), LocalDateTime.now());
    }

    @Override
    @Cacheable(value = "wallets", key = "#walletId")
    public WalletResponseDto getWalletDetails(UUID walletId) {
        Wallet wallet = findWalletById(walletId);
        return new WalletResponseDto(wallet.getWalletId(), wallet.getBalance(), LocalDateTime.now());
    }

    private Wallet findWalletById(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet not found"));
    }

    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }
}
