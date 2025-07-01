package com.test.library.main.service;

import com.test.library.main.dto.request.CreateTransactionRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
    public UUID createTransaction(CreateTransactionRequestDto request) {
        //TODO create transaction procedures
        return UUID.randomUUID();
    }

    @Async
    public CompletableFuture<String> createPayment(UUID transactionId, UUID paymentID) throws InterruptedException {
        log.debug("Sending payment request to vendor");
        //TODO create payment scheme
        return CompletableFuture.completedFuture(UUID.randomUUID().toString());
    }
}
