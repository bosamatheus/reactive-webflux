package com.github.bosamatheus.reactivewebflux.payment.listeners;

import com.github.bosamatheus.reactivewebflux.payment.models.PubSubMessage;
import com.github.bosamatheus.reactivewebflux.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.time.Duration;

import static com.github.bosamatheus.reactivewebflux.payment.models.Payment.PaymentStatus.APPROVED;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener implements InitializingBean {

    private final Sinks.Many<PubSubMessage> sink;
    private final PaymentService service;

    @Override
    public void afterPropertiesSet() {
        sink.asFlux()
            .delayElements(Duration.ofMillis(100))
            .subscribe(
                next -> {
                    log.info("On next message - {}", next.key());
                    service.processPayment(next.key(), APPROVED)
                        .doOnNext(it -> log.info("Payment processed on listener"))
                        .subscribe();
                },
                error -> log.error("On pub-sub listener observe error", error),
                () -> log.info("On pub-sub listener complete")
          );
    }

}
