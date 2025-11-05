package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.domain.SubscriptionRepository;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

	private final SubscriptionJpaRepository subscriptionJpaRepository;

	@Override
	public Subscription save(Subscription subscription) {
		SubscriptionEntity entity = SubscriptionEntity.fromDomain(subscription);

		SubscriptionEntity savedEntity = subscriptionJpaRepository.save(entity);

		return savedEntity.toDomain();
	}

}
