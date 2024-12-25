package com.test.wallet_service.walletImpl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.wallet_service.api.dto.WalletOperationRequestDto;
import com.test.wallet_service.api.dto.WalletResponseDto;
import com.test.wallet_service.api.service.WalletService;
import com.test.wallet_service.impl.controller.WalletControllerImpl;
import com.test.wallet_service.impl.exception.NotFoundException;
import com.test.wallet_service.impl.exception.OperationValidationException;
import com.test.wallet_service.impl.handler.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static com.test.wallet_service.TestGenerator.generateWalletResponseDto;
import static com.test.wallet_service.TestGenerator.generateDepositWalletOperationDto;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletControllerImpl.class)
public class WalletControllerImplTest {

    @MockitoBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private WalletOperationRequestDto requestDto;

    public static final String URL_WALLET_OPERATION = "/api/v1/wallet";
    public static final String URL_WALLETS_DETAILS = "/api/v1/wallets";

    public static final WalletResponseDto WALLET_RESPONSE_DTO = generateWalletResponseDto();
    public static final WalletOperationRequestDto WALLET_DEPOSIT_REQUEST_DTO = generateDepositWalletOperationDto();

    @Test
    @DisplayName("POST /api/v1/wallet should return 200 OK and WalletResponseDto")
    void testPerformWalletOperation_ShouldReturnOk() throws Exception {
        when(walletService.performOperation(any(WalletOperationRequestDto.class))).thenReturn(WALLET_RESPONSE_DTO);

        mockMvc.perform(post(URL_WALLET_OPERATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WALLET_DEPOSIT_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(WALLET_RESPONSE_DTO.getWalletId().toString()))
                .andExpect(jsonPath("$.balance").value(WALLET_RESPONSE_DTO.getBalance().doubleValue()))
                .andExpect(jsonPath("$.completedAt").exists());
    }

    @ParameterizedTest
    @MethodSource("exceptionsProvider")
    @DisplayName("POST /api/v1/wallets should return error if service thrown")
    void testPerformWalletOperation_ShouldReturnErrorResponseIfServiceThrownException(
            Throwable ex, HttpStatus httpStatus) throws Exception {
        ErrorResponse expected = new ErrorResponse(httpStatus, ex.getMessage());
        when(walletService.performOperation(any(WalletOperationRequestDto.class))).thenThrow(ex);

        mockMvc.perform(post(URL_WALLET_OPERATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(WALLET_DEPOSIT_REQUEST_DTO)))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    ErrorResponse actual = objectMapper.readValue(jsonResponse, ErrorResponse.class);
                    assertEquals(expected.getCode(), actual.getCode());
                });
    }

    static Stream<Arguments> exceptionsProvider() {
        return Stream.of(
                Arguments.of(new NotFoundException("NOT FOUND"), HttpStatus.NOT_FOUND),
                Arguments.of(new OperationValidationException("CONFLICT"), HttpStatus.CONFLICT)
        );
    }

    @Test
    @DisplayName("GET /api/v1/wallets/{walletId} should return 200 OK and WalletResponseDto")
    void testGetWalletDetails_ShouldReturnOk() throws Exception {
        UUID walletId = UUID.randomUUID();
        when(walletService.getWalletDetails(walletId)).thenReturn(WALLET_RESPONSE_DTO);

        mockMvc.perform(get(URL_WALLETS_DETAILS + "/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(WALLET_RESPONSE_DTO.getWalletId().toString()))
                .andExpect(jsonPath("$.balance").value(WALLET_RESPONSE_DTO.getBalance().doubleValue()))
                .andExpect(jsonPath("$.completedAt").exists());

        verify(walletService, times(1)).getWalletDetails(walletId);
    }

    @Test
    @DisplayName("GET /api/v1/wallets/{walletId} should return 404 NOT FOUND")
    void testGetWalletDetails_ShouldReturnNotFoundException() throws Exception {
        UUID walletId = UUID.randomUUID();
        ErrorResponse expected = new ErrorResponse(HttpStatus.NOT_FOUND, "Not found");
        when(walletService.getWalletDetails(walletId)).thenThrow(new NotFoundException("Not found"));

        mockMvc.perform(get(URL_WALLETS_DETAILS + "/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    ErrorResponse actual = objectMapper.readValue(jsonResponse, ErrorResponse.class);
                    assertEquals(expected.getCode(), actual.getCode());
                });
    }
}
