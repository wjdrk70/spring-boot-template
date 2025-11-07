package com.nexsol.cargo.core.domain;

import lombok.Builder;

import java.util.Set;

@Builder
public record CreateSubscription(Long userId, String quotationKey, Set<String> coverageCodes, boolean isSame,
		String policyholderCompanyName, String policyholderCompanyCode, String insuredCompanyName,
		String insuredCompanyCode

) {
}
