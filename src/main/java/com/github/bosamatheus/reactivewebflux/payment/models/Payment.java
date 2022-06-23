package com.github.bosamatheus.reactivewebflux.payment.models;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Payment(
    String paymentId,
    String userId,
    PaymentStatus paymentStatus,
    LocalDateTime createdAt
) {

    public enum PaymentStatus {
        PENDING,
        APPROVED
    }

}
