package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

	private final SubscriptionAppender subscriptionAppender;

	private final SubscriptionReader subscriptionReader;

	private final SubscriptionWriter subscriptionWriter;

	private final QuotationReader quotationReader;

	private final CoverageMasterReader coverageMasterReader;

	private final SubscriptionCoverageManager subscriptionCoverageManager;

	private final PremiumCalculator premiumCalculator;

	@Transactional
	public SubscriptionResult create(CreateSubscription creation) {
		Quotation quotation = quotationReader.read(creation.quotationKey());

		CoverageMaster masterSet = coverageMasterReader.read(creation.coverageCodes());

		List<SubscriptionCoverage> coveragesToSave = subscriptionCoverageManager.create(creation.coverageCodes());

		CargoDetail cargoDetail = quotation.toCargoDetail();

		BigDecimal finalPremium = premiumCalculator.calculate(cargoDetail, quotation.getExchangeRateAmount(),
				masterSet.baseCoverage(), masterSet.options());

		Subscription savedSubscription = subscriptionAppender.append(creation, quotation, coveragesToSave,
				finalPremium);

		return new SubscriptionResult(savedSubscription.getId(), savedSubscription.getInsurancePremium());
	}

	@Transactional
	public Subscription issuePolicy(Long userId, Long subscriptionId) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		if (subscription.getStatus() == SubscriptionStatus.POLICY_ISSUED) {
			return subscription;
		}

		if (subscription.getStatus() != SubscriptionStatus.PAYMENT_COMPLETE) {
			throw new CoreException(CoreErrorType.POLICY_CANNOT_BE_ISSUED);
		}

		String policyNumber = String.format("DB-%d-%06d", LocalDate.now().getYear(), subscription.getId());

		subscription.issuePolicy(policyNumber);

		Subscription issuedSubscription = subscriptionWriter.write(subscription);

		return issuedSubscription;
	}

}
