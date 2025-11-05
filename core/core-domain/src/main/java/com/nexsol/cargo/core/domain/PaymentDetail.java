package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentDetail(
        Long subscriptionId,
        BigDecimal insurancePremium,
        String paymentMethod,
        String cardType,
        String cardNumberMasked,
        String expiryDate,
        PaymentStatus paymentStatus,
        String externalPaymentKey


) {
}
