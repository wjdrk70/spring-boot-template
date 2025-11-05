package com.nexsol.cargo.core.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * @param planName e.g., "플랜 A"
 * @param baseCoverage 기본 담보 (1개)
 * @param optionCoverages 옵션 담보 (N개)
 * @param premium 계산된 최종 보험료
 */
public record RecommendPlan(String planName, BaseCoverage baseCoverage, List<OptionCoverage> optionCoverages,
		BigDecimal premium) {

}
