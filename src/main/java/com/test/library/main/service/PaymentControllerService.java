package com.test.library.main.service;

import com.test.library.main.dto.request.CreateTransactionRequestDto;
import com.test.library.main.dto.response.CreateTransactionResponseDto;
import com.test.library.main.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentControllerService {
    final PaymentService paymentService;

    public CreateTransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        UUID transactionId = paymentService.createTransaction(request);
        return new CreateTransactionResponseDto(transactionId);
    }

    public PaymentResponse createPayment(UUID transactionId) {
        UUID paymentId = UUID.randomUUID();
        try {
            paymentService.createPayment(transactionId, paymentId)
                    .thenAccept(vendorPaymentCode -> {
                        //TODO tie transactionId, paymentId, and vendorPaymentCode together
                    })
                    .exceptionally(e -> {
                        log.error("{}", e.getMessage(), e);
                        return null;
                    });
            return new PaymentResponse(paymentId);
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    "Error while processing payment for transaction=" + transactionId +
                    " with paymentId=" + transactionId + ": " + e.getMessage(), e);
        }
    }
}
