package com.github.bosamatheus.reactivewebflux.payment.dto;

import com.github.bosamatheus.reactivewebflux.payment.models.Payment;
import lombok.Builder;

@Builder
public record CreatePaymentResponse(
    String paymentId,
    String userId
) {

    public static CreatePaymentResponse mapDomainToDTO(final Payment payment) {
        return CreatePaymentResponse.builder()
            .paymentId(payment.paymentId())
            .userId(payment.userId())
            .build();
    }

}
