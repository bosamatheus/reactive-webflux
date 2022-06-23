package com.github.bosamatheus.reactivewebflux.payment.dto;

import com.github.bosamatheus.reactivewebflux.payment.models.Payment;
import lombok.Builder;

@Builder
public record CreatePaymentResponse(
    String paymentId,
    String userId,
    String paymentStatus
) {

    public static CreatePaymentResponse mapDomainToDTO(final Payment payment) {
        return CreatePaymentResponse.builder()
            .paymentId(payment.paymentId())
            .userId(payment.userId())
            .paymentStatus(payment.paymentStatus().name())
            .build();
    }

}
