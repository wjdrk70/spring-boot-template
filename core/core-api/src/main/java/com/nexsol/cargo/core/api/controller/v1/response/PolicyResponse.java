package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.enums.ConditionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PolicyResponse {

	private final String policyNumber;

	private final String policyholderCompanyName; // 계약자

	private final String insuredCompanyName; // 피보험자

	private final BigDecimal insurancePremium; // 보험료 (Premium)

	private final String status;

	private final CargoInfoResponse cargoInfo;

	private final CoverageInfoResponse baseCoverage;

	private final List<CoverageInfoResponse> referenceCoverages;

	private final List<CoverageInfoResponse> additionalCoverages;

	private final List<CoverageInfoResponse> extensionCoverages;

	public static PolicyResponse of(Subscription subscription) {

		String policyNumber = subscription.getPolicyNumber();
		String policyholderCompanyName = subscription.getPolicyholderCompanyName();

		String insuredCompanyName = subscription.isSame() ? subscription.getPolicyholderCompanyName()
				: subscription.getInsuredCompanyName();
		BigDecimal insurancePremium = subscription.getInsurancePremium();
		String status = subscription.getStatus().name();

		CargoInfoResponse cargoInfo = CargoInfoResponse.of(subscription.getCargoDetail());

		List<CoverageInfoResponse> allCoverages = subscription.getSubscriptionCoverages()
			.stream()
			.map(CoverageInfoResponse::of)
			.collect(Collectors.toList());

		CoverageInfoResponse baseCoverage = allCoverages.stream()
			.filter(c -> c.getConditionType().equals(ConditionType.BASE.name()))
			.findFirst()
			.orElse(null);

		List<CoverageInfoResponse> referenceCoverages = allCoverages.stream()
			.filter(c -> c.getConditionType().equals(ConditionType.REFERENCE.name()))
			.collect(Collectors.toList());

		List<CoverageInfoResponse> additionalCoverages = allCoverages.stream()
			.filter(c -> c.getConditionType().equals(ConditionType.ADDITIONAL.name()))
			.collect(Collectors.toList());

		List<CoverageInfoResponse> extensionCoverages = allCoverages.stream()
			.filter(c -> c.getConditionType().equals(ConditionType.EXTENSION.name()))
			.collect(Collectors.toList());

		List<CoverageInfoResponse> coverages = subscription.getSubscriptionCoverages()
			.stream()
			.map(CoverageInfoResponse::of)
			.collect(Collectors.toList());

		return new PolicyResponse(policyNumber, policyholderCompanyName, insuredCompanyName, insurancePremium, status,
				cargoInfo, baseCoverage, referenceCoverages, additionalCoverages, extensionCoverages);
	}

}