package com.github.bosamatheus.reactivewebflux.payment.repositories;

import com.github.bosamatheus.reactivewebflux.payment.models.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final Database db;

    public Mono<Payment> savePayment(final Payment payment) {
        return Mono.fromCallable(() -> {
                log.info("Saving payment transaction for user {}", payment.userId());
                return db.save(payment.paymentId(), payment);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(next -> log.info("Payment received {}", next.paymentId()));
    }

    public Mono<Payment> getPayment(final String paymentId) {
        return Mono.defer(() -> {
                log.info("Getting payment {} from database", paymentId);
                final Optional<Payment> payment = db.get(paymentId, Payment.class);
                return Mono.justOrEmpty(payment);
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

}
