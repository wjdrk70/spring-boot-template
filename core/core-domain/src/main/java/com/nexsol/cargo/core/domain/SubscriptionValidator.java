package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SubscriptionValidator {

	private final CoverageMasterReader coverageMasterReader;

	private final PremiumCalculator premiumCalculator;

	public BigDecimal validatePremium(CreateSubscription creation) {
		// 필수 담보 포함되어 있는 검증
		CoverageMaster masterSet = coverageMasterReader.findValidatedMaster(creation.coverageCodes());

		// front 금액 다시 검증
		return premiumCalculator.calculate(creation.invoiceAmount(), masterSet.baseCoverage(), masterSet.options(),
				creation.currencyUnit(), creation.exchangeRateAmount());
	}

}
