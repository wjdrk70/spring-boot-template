package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.CargoDetail;
import com.nexsol.cargo.core.domain.CoverageSnapshot;
import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.domain.SubscriptionRepository;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCargoEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCoverageEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final SubscriptionCargoJpaRepository subscriptionCargoJpaRepository;
    private final SubscriptionCoverageJpaRepository subscriptionCoverageJpaRepository;

	@Override
	public Subscription save(Subscription subscription) {
        SubscriptionEntity entity = SubscriptionEntity.fromDomain(subscription);
        SubscriptionEntity savedEntity = subscriptionJpaRepository.save(entity);

        Long newSubscriptionId = savedEntity.getId();

        SubscriptionCargoEntity cargoEntity = SubscriptionCargoEntity.fromDomain(
                subscription.cargoDetail(),
                newSubscriptionId
        );
        subscriptionCargoJpaRepository.save(cargoEntity);

        List<SubscriptionCoverageEntity> coverageEntities = subscription.snapshots()
                .stream()
                .map(snapshot -> SubscriptionCoverageEntity.fromDomain(snapshot, newSubscriptionId))
                .collect(Collectors.toList());

        if (!coverageEntities.isEmpty()) {
            subscriptionCoverageJpaRepository.saveAll(coverageEntities);
        }

        return new Subscription(
                newSubscriptionId,
                subscription.userId(),
                subscription.status(),
                subscription.isSame(),
                subscription.policyholderCompanyName(),
                subscription.policyholderCompanyCode(),
                subscription.insuredCompanyName(),
                subscription.insuredCompanyCode(),
                subscription.cargoDetail(),
                subscription.snapshots()
        );
	}

    @Override
    public Optional<Subscription> findById(Long subscriptionId) {
        Optional<SubscriptionEntity> subEntityOpt = subscriptionJpaRepository.findById(subscriptionId);
        if (subEntityOpt.isEmpty()) {
            return Optional.empty();
        }

        SubscriptionEntity subEntity = subEntityOpt.get();

        CargoDetail cargoDetail = subscriptionCargoJpaRepository.findById(subscriptionId)
                .map(SubscriptionCargoEntity::toDomain)
                .orElse(null); // (존재해야 함)

        List<CoverageSnapshot> snapshots = subscriptionCoverageJpaRepository.findBySubscriptionId(subscriptionId)
                .stream()
                .map(SubscriptionCoverageEntity::toDomain)
                .collect(Collectors.toList());

        Subscription subscription = new Subscription(
                subscriptionId,
                subEntity.getUserId(),
                subEntity.getStatus(),
                subEntity.getIsSame(),
                subEntity.getPolicyholderCompanyName(),
                subEntity.getPolicyholderCompanyCode(),
                subEntity.getInsuredCompanyName(),
                subEntity.getInsuredCompanyCode(),
                cargoDetail,
                snapshots
        );
        return Optional.of(subscription);
    }

}
