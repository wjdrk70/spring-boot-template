package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlanGenerator {

	private final CoverageMasterReader masterReader;

	private final PremiumCalculator premiumCalculator;

	private static final String FIRST_PLAN_NAME = "플랜";

	private static final char SECOND_PLAN_NAME = 'A';

	public List<RecommendPlan> generatePlan(List<Set<String>> topCoverageSets, Quotation quotation) {

		AtomicInteger rank = new AtomicInteger(0);
		CargoDetail cargoDetail = quotation.toCargoDetail();

		return topCoverageSets.stream().map(codeSet -> {

			CoverageMaster masterSet = masterReader.read(codeSet);

			BigDecimal premium = premiumCalculator.calculate(cargoDetail, quotation.getExchangeRateAmount(),
					masterSet.baseCoverage(), masterSet.options());
			String planName = FIRST_PLAN_NAME + (char) (SECOND_PLAN_NAME + rank.getAndIncrement());

			return new RecommendPlan(planName, masterSet.baseCoverage(), masterSet.options(), premium);

		}).collect(Collectors.toList());
	}

}
