package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.util.List;

public record Subscription(
        Long id,
        Long userId,
        SubscriptionStatus status,
        BigDecimal insurancePremium,
        BigDecimal invoiceAmount, // 송장가액
        String currencyUnit, // 화폐단위
        String hsCode,
        boolean isSamePolicyholderAndInsured,
        String policyholderCompanyName,
        String policyholderCompanyCode,
        String insuredCompanyName,
        String insuredCompanyCode,


        CargoDetail cargoDetail,


        PaymentDetail paymentDetail,


        List<CoverageSnapshot> snapshots
) {
}
