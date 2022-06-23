package com.github.bosamatheus.reactivewebflux.payment.config;

import com.github.bosamatheus.reactivewebflux.payment.models.PubSubMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class PubSubMessageConfig {

    @Bean
    public Sinks.Many<PubSubMessage> sink() {
        return Sinks.many()
            .multicast()
            .onBackpressureBuffer();
    }

}
