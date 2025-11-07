package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionAppender {

	private final SubscriptionRepository subscriptionRepository;

	private final QuotationRepository quotationRepository;

	private final QuotationReader quotationReader;

	private final CoverageMasterReader coverageMasterReader;

	private final SubscriptionCoverageManager subscriptionCoverageManager;

	private final PremiumCalculator premiumCalculator;

	public Subscription append(CreateSubscription creation) {

		Quotation quotation = quotationReader.read(creation.quotationKey());

		CargoDetail cargoDetail = quotation.toCargoDetail();

		CoverageMaster masterSet = coverageMasterReader.findValidatedMaster(creation.coverageCodes());

		List<SubscriptionCoverage> coveragesToSave = subscriptionCoverageManager.create(creation.coverageCodes());

		BigDecimal finalPremium = premiumCalculator.calculate(cargoDetail, quotation.getExchangeRateAmount(),
				masterSet.baseCoverage(), masterSet.options());

		Subscription subscription = Subscription.builder()
			.id(null)
			.userId(creation.userId())
			.status(SubscriptionStatus.PAYMENT_PENDING)
			.insurancePremium(finalPremium) // ⬅️ '최종' 확정 보험료
			.isSame(creation.isSame())
			.policyholderCompanyName(creation.policyholderCompanyName())
			.policyholderCompanyCode(creation.policyholderCompanyCode())
			.insuredCompanyName(creation.insuredCompanyName())
			.insuredCompanyCode(creation.insuredCompanyCode())
			.cargoDetail(cargoDetail)
			.subscriptionCoverages(coveragesToSave) // ⬅️ '저장용' 스냅샷
			.build();

		Subscription savedSubscription = subscriptionRepository.save(subscription);
		quotation.subscribe();
		quotationRepository.save(quotation);

		return savedSubscription;
	}

}
