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

	private final CoverageSetFilter coverageSetFilter;

	private final CargoItemFinder cargoItemFinder;

	private final PlanAnalyzer planAnalyzer;

	private final PlanGenerator planGenerator;

	private static final int TOP_N = 3;

	public List<RecommendPlan> recommendPlans(String quotationKey) {

		Quotation quotation = quotationReader.read(quotationKey);

		String middleCode = cargoItemFinder.find(quotation.getHsCode());

		List<SubscriptionCoverageSet> coverageSets = subscriptionCoverageReader.readCoverageSet(middleCode);

		List<SubscriptionCoverageSet> filterSets = coverageSetFilter.filter(coverageSets, quotation.getConveyance());

		List<Set<String>> topCoverageSets = planAnalyzer.getTopFrequentCoverageSets(filterSets, TOP_N);

		if (topCoverageSets.isEmpty()) {
			return Collections.emptyList();
		}

		return planGenerator.generatePlan(topCoverageSets, quotation);
	}

}
