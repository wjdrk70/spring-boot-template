package com.nexsol.cargo.core.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

	private final SubscriptionValidator subscriptionValidator;

	private final SubscriptionProcessor processor;

	@Transactional
	public SubscriptionResult create(CreateSubscription creation) {
		BigDecimal premium = subscriptionValidator.validatePremium(creation);

		Subscription savedSubscription = processor.createSubscription(creation, premium);

        return new SubscriptionResult(savedSubscription.id(), premium);
	}

}
