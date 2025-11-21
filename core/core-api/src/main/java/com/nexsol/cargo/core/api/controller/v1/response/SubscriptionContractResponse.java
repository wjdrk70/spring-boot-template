package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.SubscriptionContract;
import com.nexsol.cargo.core.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionContractResponse(Long subscriptionId, String policyNumber, SubscriptionStatus status,
		String policyholderName, String businessNumber, String managerName, String managerPhone, LocalDate contractDate,
		LocalDate startDate, LocalDate endDate, BigDecimal premium, String paymentMethod) {

	public static SubscriptionContractResponse of(SubscriptionContract domain) {
		return new SubscriptionContractResponse(domain.subscriptionId(), domain.policyNumber(), domain.status(),
				domain.policyholderName(), domain.businessNumber(), domain.managerName(), domain.managerPhone(),
				domain.contractDate(), domain.startDate(), domain.endDate(), domain.premium(), domain.paymentMethod());
	}
}