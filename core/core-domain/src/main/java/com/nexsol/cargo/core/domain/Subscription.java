package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record Subscription(Long id, Long userId, SubscriptionStatus status, BigDecimal insurancePremium,
		BigDecimal invoiceAmount, // 송장가액
		String currencyUnit, // 화폐단위
		String hsCode, boolean isSame, String policyholderCompanyName, String policyholderCompanyCode,
		String insuredCompanyName, String insuredCompanyCode,

		CargoDetail cargoDetail,

		List<CoverageSnapshot> snapshots) {

}
