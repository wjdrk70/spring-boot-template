package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.support.DomainPage;
import com.nexsol.cargo.core.support.DomainPageRequest;

import java.util.Optional;

public interface SubscriptionRepository {

	Subscription save(Subscription subscription);

	Optional<Subscription> findById(Long subscriptionId);

	DomainPage<Subscription> findAllByUserId(Long userId, DomainPageRequest pageRequest);

	DomainPage<Subscription> searchByContract(Long userId, SubscriptionSearch contract, DomainPageRequest pageRequest);

}
