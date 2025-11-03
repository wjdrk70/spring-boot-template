package com.nexsol.cargo.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class RecommendPlan {

    private final String planName; // e.g., "플랜 A"
    private final BaseCoverage baseCoverage; // 기본 담보 (1개)
    private final List<OptionCoverage> optionCoverages; // 옵션 담보 (N개)
    private final BigDecimal premium; // 계산된 최종 보험료
}
