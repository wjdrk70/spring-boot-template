package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionAppender {

	private final SubscriptionRepository subscriptionRepository;

	private final ApplicationEventPublisher eventPublisher;

	public Subscription append(CreateSubscription creation, Quotation quotation,
			List<SubscriptionCoverage> coveragesToSave, BigDecimal finalPremium) {

		Subscription subscription = Subscription.builder()
			.id(null)
			.userId(creation.userId())
			.status(SubscriptionStatus.PAYMENT_PENDING)
			.insurancePremium(finalPremium)
			.isSame(creation.isSame())
			.policyholderCompanyName(creation.policyholderCompanyName())
			.policyholderCompanyCode(creation.policyholderCompanyCode())
			.insuredCompanyName(creation.insuredCompanyName())
			.insuredCompanyCode(creation.insuredCompanyCode())
			.cargoDetail(quotation.toCargoDetail())
			.subscriptionCoverages(coveragesToSave)
			.build();

		Subscription savedSubscription = subscriptionRepository.save(subscription);
		// quotation.subscribe();
		// quotationRepository.save(quotation);

		eventPublisher.publishEvent(new SubscriptionCreatEvent(quotation.getQuotationKey()));

		return savedSubscription;
	}

}
