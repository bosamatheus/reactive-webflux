package com.github.bosamatheus.reactivewebflux.payment.services;

import com.github.bosamatheus.reactivewebflux.payment.models.Payment;
import com.github.bosamatheus.reactivewebflux.payment.models.Payment.PaymentStatus;
import com.github.bosamatheus.reactivewebflux.payment.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static com.github.bosamatheus.reactivewebflux.payment.models.Payment.PaymentStatus.PENDING;

@Slf4j
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

    public Mono<Payment> getPayment(final String paymentId) {
        return repository.getPayment(paymentId);
    }

    public Mono<Payment> processPayment(final String paymentId, final PaymentStatus status) {
        log.info("On process payment {} to status {}", paymentId, status);
        return getPayment(paymentId)
            .flatMap(payment -> {
                log.info("Processing payment {} to status {}", paymentId, status);
                return repository.savePayment(payment.withPaymentStatus(status));
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    public Set<String> getPaymentsIds() {
        return repository.getPaymentIds();
    }

}
