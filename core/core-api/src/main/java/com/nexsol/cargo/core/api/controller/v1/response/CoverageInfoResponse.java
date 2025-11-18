package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.SubscriptionCoverage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CoverageInfoResponse {

	private final String conditionType;

	private final String conditionName;

	private final String conditionCode;

	public static CoverageInfoResponse of(SubscriptionCoverage coverage) {
		if (coverage == null) {
			return null;
		}
		return new CoverageInfoResponse(coverage.conditionType().name(), coverage.conditionName(),
				coverage.conditionCode());
	}

}