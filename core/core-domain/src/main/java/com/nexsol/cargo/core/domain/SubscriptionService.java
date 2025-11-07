package com.nexsol.cargo.core.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

	private final SubscriptionAppender subscriptionAppender;

	@Transactional
	public SubscriptionResult create(CreateSubscription creation) {
		// BigDecimal premium = subscriptionValidator.validatePremium(creation);

		Subscription savedSubscription = subscriptionAppender.append(creation);

		return new SubscriptionResult(savedSubscription.getId(), savedSubscription.getInsurancePremium());
	}

}
