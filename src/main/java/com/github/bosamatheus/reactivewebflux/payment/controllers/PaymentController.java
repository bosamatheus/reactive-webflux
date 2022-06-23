package com.github.bosamatheus.reactivewebflux.payment.controllers;

import com.github.bosamatheus.reactivewebflux.payment.dto.CreatePaymentRequest;
import com.github.bosamatheus.reactivewebflux.payment.dto.CreatePaymentResponse;
import com.github.bosamatheus.reactivewebflux.payment.publishers.PaymentPublisher;
import com.github.bosamatheus.reactivewebflux.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static com.github.bosamatheus.reactivewebflux.payment.models.Payment.PaymentStatus.APPROVED;

@Slf4j
@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    private final PaymentPublisher publisher;

    @PostMapping
    public Mono<ResponseEntity<CreatePaymentResponse>> createPayment(@RequestBody final CreatePaymentRequest createPaymentRequest) {
        final String userId = createPaymentRequest.userId();
        log.info("Payment to be processed for user {}", userId);
        return service.createPayment(userId)
            .flatMap(publisher::onPaymentCreate)
            // Pooling
            .flatMap(payment ->
                    Flux.interval(Duration.ofSeconds(1))
                        .doOnNext(next -> log.info("Next tick {}", next))
                        .flatMap(tick -> service.getPayment(payment.paymentId()))
                        .filter(p -> APPROVED == p.paymentStatus())
                        .next()
                    )
            .doOnNext(next -> log.info("Payment processed {}", next.paymentId()))
            .map(payment -> ResponseEntity.ok(CreatePaymentResponse.mapDomainToDTO(payment)))
            .timeout(Duration.ofSeconds(5))
            .retryWhen(
                Retry.backoff(2, Duration.ofSeconds(1))
                    .doAfterRetry(signal -> log.info("Execution failed. Retrying... {}", signal.totalRetries()))
            );
    }

}
