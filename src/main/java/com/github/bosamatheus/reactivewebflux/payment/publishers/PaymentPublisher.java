package com.github.bosamatheus.reactivewebflux.payment.publishers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bosamatheus.reactivewebflux.payment.models.Payment;
import com.github.bosamatheus.reactivewebflux.payment.models.PubSubMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class PaymentPublisher {

    private final Sinks.Many<PubSubMessage> sink;
    private final ObjectMapper mapper;

    public Mono<Payment> onPaymentCreate(final Payment payment) {
        return Mono.fromCallable(() -> {
                final String paymentId = payment.paymentId();
                final String data = mapper.writeValueAsString(payment);
                return new PubSubMessage(paymentId, data);
            })
            .subscribeOn(Schedulers.parallel())
            .doOnNext(sink::tryEmitNext)
            .thenReturn(payment);
    }

}
