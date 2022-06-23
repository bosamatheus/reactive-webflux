package com.github.bosamatheus.reactivewebflux.payment.controllers;

import com.github.bosamatheus.reactivewebflux.payment.dto.CreatePaymentRequest;
import com.github.bosamatheus.reactivewebflux.payment.dto.CreatePaymentResponse;
import com.github.bosamatheus.reactivewebflux.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public Mono<ResponseEntity<CreatePaymentResponse>> createPayment(@RequestBody final CreatePaymentRequest createPaymentRequest) {
        final String userId = createPaymentRequest.userId();
        log.info("Payment to be processed for user {}", userId);
        return service.createPayment(userId)
            .doOnNext(next -> log.info("Payment processed {}", next.paymentId()))
            .map(payment -> ResponseEntity.ok(CreatePaymentResponse.mapDomainToDTO(payment)));
    }

}
