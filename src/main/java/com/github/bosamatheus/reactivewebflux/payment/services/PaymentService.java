package com.github.bosamatheus.reactivewebflux.payment.services;

import com.github.bosamatheus.reactivewebflux.payment.models.Payment;
import com.github.bosamatheus.reactivewebflux.payment.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.bosamatheus.reactivewebflux.payment.models.Payment.PaymentStatus.PENDING;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    public Mono<Payment> createPayment(final String userId) {
        final var payment = Payment.builder()
            .paymentId(UUID.randomUUID().toString())
            .userId(userId)
            .paymentStatus(PENDING)
            .createdAt(LocalDateTime.now())
            .build();
        return repository.savePayment(payment);
    }

}
