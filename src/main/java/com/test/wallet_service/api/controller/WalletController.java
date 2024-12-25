package com.test.wallet_service.api.controller;

import com.test.wallet_service.api.dto.WalletOperationRequestDto;
import com.test.wallet_service.api.dto.WalletResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping(value = "api/v1")
public interface WalletController {

    @PostMapping("/wallet")
    ResponseEntity<WalletResponseDto> performWalletOperation(@RequestBody WalletOperationRequestDto request);

    @GetMapping("/wallets/{walletId}")
    ResponseEntity<WalletResponseDto> getWalletDetails(@PathVariable UUID walletId);

}
