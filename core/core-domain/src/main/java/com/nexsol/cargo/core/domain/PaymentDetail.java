package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentStatus;

public record PaymentDetail(String paymentMethod,
                            String cardType,
                            String cardNumberMasked,
                            String expiryDate,
                            PaymentStatus paymentStatus) {
}
