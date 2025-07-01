package com.test.library.main.controller;

import com.test.library.main.dto.request.CreateTransactionRequestDto;
import com.test.library.main.dto.response.CreateTransactionResponseDto;
import com.test.library.main.dto.response.PaymentResponse;
import com.test.library.main.service.PaymentControllerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Validated
@Log4j2
public class PaymentController {
    final PaymentControllerService paymentControllerService;

    @PostMapping
    public CreateTransactionResponseDto createTransaction(
            @RequestBody @NotNull @Valid CreateTransactionRequestDto request) {
        log.debug("INCOMING REQUEST to Payment.createTransaction: {}", request);
        return paymentControllerService.createTransaction(request);
    }

    @PostMapping("/{transactionId}")
    public PaymentResponse createPayment(@PathVariable @NotNull UUID transactionId) {
        log.debug("INCOMING REQUEST to Payment.createPayment: transactionId={}", transactionId);
        return paymentControllerService.createPayment(transactionId);
    }
}
