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
public class PlanProcessor {

	private final CoverageMasterReader masterReader;

	private final PremiumCalculator premiumCalculator;

	private static final String FIRST_PLAN_NAME = "플랜";

	private static final char SECOND_PLAN_NAME = 'A';

	public List<RecommendPlan> assemblePlans(List<Set<String>> topCoverageSets, BigDecimal invoiceAmount,
			String currencyUnit, BigDecimal exchangeRateAmount) {

		AtomicInteger rank = new AtomicInteger(0);

		return topCoverageSets.stream().map(codeSet -> {

			CoverageMaster masterSet = masterReader.findValidatedMaster(codeSet);

			// 보험료 계산
			BigDecimal premium = premiumCalculator.calculate(invoiceAmount, masterSet.baseCoverage(),
					masterSet.options(), currencyUnit, exchangeRateAmount);

			String planName = FIRST_PLAN_NAME + (char) (SECOND_PLAN_NAME + rank.getAndIncrement());
			return new RecommendPlan(planName, masterSet.baseCoverage(), masterSet.options(), premium);
		}).collect(Collectors.toList());
	}

}
