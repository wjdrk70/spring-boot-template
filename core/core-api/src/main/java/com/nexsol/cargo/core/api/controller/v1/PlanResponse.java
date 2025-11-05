package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.response.CoverageResponse;
import com.nexsol.cargo.core.domain.BaseCoverage;
import com.nexsol.cargo.core.domain.OptionCoverage;
import com.nexsol.cargo.core.domain.RecommendPlan;
import com.nexsol.cargo.core.enums.CoverageOptionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PlanResponse {

	private String planName; // e.g., "플랜 A"

	private BigDecimal premium; // e.g., 21090

	private CoverageResponse baseCoverage;

	private List<CoverageResponse> referenceCoverages; // 참조조건

	private List<CoverageResponse> additionalCoverages; // 추가조건

	private List<CoverageResponse> extensionCoverages; // 확장조건

	public static PlanResponse fromDomain(RecommendPlan plan) {
		BaseCoverage base = plan.baseCoverage();
		List<OptionCoverage> options = plan.optionCoverages();

		return PlanResponse.builder()
			.planName(plan.planName())
			.premium(plan.premium())
			.baseCoverage(CoverageResponse.builder().code(base.getCode()).name(base.getName()).build())
			.referenceCoverages(filterOptions(options, CoverageOptionType.REFERENCE))
			.additionalCoverages(filterOptions(options, CoverageOptionType.ADDITIONAL))
			.extensionCoverages(filterOptions(options, CoverageOptionType.EXTENSION))
			.build();
	}

	private static List<CoverageResponse> filterOptions(List<OptionCoverage> options, CoverageOptionType type) {
		return options.stream()
			.filter(o -> o.getType() == type)
			.map(o -> CoverageResponse.builder().code(o.getCode()).name(o.getName()).build())
			.collect(Collectors.toList());
	}

}