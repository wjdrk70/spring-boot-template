package com.nexsol.cargo.core.domain;

import java.util.Optional;

public interface SubscriptionRepository {

	Subscription save(Subscription subscription);

	Optional<Subscription> findById(Long subscriptionId);

}
