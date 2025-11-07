package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlanRecommendationService {

	private final QuotationReader quotationReader;

	private final SubscriptionCoverageReader subscriptionCoverageReader;

	private final PlanAnalyzer planAnalyzer;

	private final PlanGenerator planGenerator;

	private static final int TOP_N = 3;

	public List<RecommendPlan> recommendPlans(String quotationKey) {

		Quotation quotation = quotationReader.read(quotationKey);

		// 과거 계약을 분석
		List<SubscriptionCoverageSet> coverageSets = subscriptionCoverageReader.readCoverageSet(quotation.getHsCode());

		List<Set<String>> topCoverageSets = planAnalyzer.getTopFrequentCoverageSets(coverageSets, TOP_N);

		if (topCoverageSets.isEmpty()) {
			return Collections.emptyList();
		}

		// 조합 목록을 받아, 마스터 조회 -> 계산 -> 플랜 조립 로직을 위임
		return planGenerator.generatePlan(topCoverageSets, quotation);
	}

}
