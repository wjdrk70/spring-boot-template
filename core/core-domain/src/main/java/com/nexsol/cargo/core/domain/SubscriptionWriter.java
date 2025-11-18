package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionWriter {

	private final SubscriptionRepository subscriptionRepository;

	public Subscription write(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}

}
