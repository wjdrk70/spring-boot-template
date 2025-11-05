package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentStatus;
import lombok.Builder;

@Builder
public record PaymentDetail(Long subscriptionId, String paymentMethod, String cardType, String cardNumberMasked,
		String expiryDate, PaymentStatus paymentStatus) {
}
