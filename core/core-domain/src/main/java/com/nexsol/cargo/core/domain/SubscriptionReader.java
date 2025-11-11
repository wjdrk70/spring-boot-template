package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {

	private final SubscriptionRepository subscriptionRepository;

	public Subscription read(Long subscriptionId, Long userId) {

		Subscription subscription = subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));

		if (!subscription.getUserId().equals(userId)) {

			throw new CoreException(CoreErrorType.AUTH_UNAUTHORIZED);
		}

		if (subscription.getStatus() != SubscriptionStatus.PAYMENT_PENDING) {

		}

		return subscription;
	}

	public Subscription read(Long subscriptionId) {
		return findById(subscriptionId);
	}

	private Subscription findById(Long subscriptionId) {
		return subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));
	}

}
