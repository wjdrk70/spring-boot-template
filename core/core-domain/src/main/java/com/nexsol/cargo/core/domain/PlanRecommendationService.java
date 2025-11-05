package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlanRecommendationService {

	private final PastContactReader pastContactReader;

	private final PlanProcessor planProcessor;

	private static final int TOP_N = 3;

	public List<RecommendPlan> recommendPlans(String hsCode, BigDecimal invoiceAmount, String currencyUnit,
			BigDecimal exchangeRateAmount) {

		// 과거 계약을 분석
		List<Set<String>> topCoverageSets = pastContactReader.readTopCombinations(hsCode, TOP_N);

		if (topCoverageSets.isEmpty()) {
			return Collections.emptyList();
		}

		// 조합 목록을 받아, 마스터 조회 -> 계산 -> 플랜 조립 로직을 위임
		return planProcessor.assemblePlans(topCoverageSets, invoiceAmount, currencyUnit, exchangeRateAmount);
	}

}
