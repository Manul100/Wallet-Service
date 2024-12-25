package com.test.wallet_service.impl.controller;

import com.test.wallet_service.api.controller.WalletController;
import com.test.wallet_service.api.dto.WalletOperationRequestDto;
import com.test.wallet_service.api.dto.WalletResponseDto;
import com.test.wallet_service.api.service.WalletService;
import com.test.wallet_service.db.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WalletControllerImpl implements WalletController {

    private final WalletService walletService;

    @Override
    public ResponseEntity<WalletResponseDto> performWalletOperation(@RequestBody WalletOperationRequestDto request) {
        return new ResponseEntity<>(walletService.performOperation(request), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WalletResponseDto> getWalletDetails(@PathVariable UUID walletId) {
        return new ResponseEntity<>(walletService.getWalletDetails(walletId), HttpStatus.OK);
    }

    @GetMapping("/wallets")
    public List<Wallet> findAll() {
        return walletService.findAll();
    }
}
