package com.nexsol.cargo.core.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

	private final SubscriptionAppender subscriptionAppender;

	private final QuotationReader quotationReader;

	private final CoverageMasterReader coverageMasterReader;

	private final SubscriptionCoverageManager subscriptionCoverageManager;

	private final PremiumCalculator premiumCalculator;

	@Transactional
	public SubscriptionResult create(CreateSubscription creation) {
		Quotation quotation = quotationReader.read(creation.quotationKey());

		CoverageMaster masterSet = coverageMasterReader.findValidatedMaster(creation.coverageCodes());

		List<SubscriptionCoverage> coveragesToSave = subscriptionCoverageManager.create(creation.coverageCodes());

		CargoDetail cargoDetail = quotation.toCargoDetail();

		BigDecimal finalPremium = premiumCalculator.calculate(cargoDetail, quotation.getExchangeRateAmount(),
				masterSet.baseCoverage(), masterSet.options());

		Subscription savedSubscription = subscriptionAppender.append(creation, quotation, coveragesToSave,
				finalPremium);

		return new SubscriptionResult(savedSubscription.getId(), savedSubscription.getInsurancePremium());
	}

}
