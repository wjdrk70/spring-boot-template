package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.CargoDetail;
import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.domain.SubscriptionCoverage;
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

		if (subscription.getId() == null) {
			return createNewSubscription(subscription);
		}
		else {
			return updateExistingSubscription(subscription);
		}
	}

	private Subscription updateExistingSubscription(Subscription subscription) {
		SubscriptionEntity managedEntity = subscriptionJpaRepository.findById(subscription.getId())
			.orElseThrow(() -> new RuntimeException("업데이트할 Subscription을 찾을 수 없습니다. ID: " + subscription.getId())); // TODO:
																													// CoreException으로
																													// 변경

		managedEntity.updateFromDomain(subscription);

		subscriptionJpaRepository.save(managedEntity);

		return findById(subscription.getId()).orElseThrow(() -> new RuntimeException("업데이트 후 Subscription 조회 실패")); // TODO:
																													// CoreException으로
																													// 변경
	}

	private Subscription createNewSubscription(Subscription subscription) {

		SubscriptionEntity entity = SubscriptionEntity.fromDomain(subscription);

		SubscriptionEntity savedEntity = subscriptionJpaRepository.save(entity);
		Long newSubscriptionId = savedEntity.getId();

		SubscriptionCargoEntity cargoEntity = SubscriptionCargoEntity.fromDomain(subscription.getCargoDetail(),
				newSubscriptionId);
		subscriptionCargoJpaRepository.save(cargoEntity);

		List<SubscriptionCoverageEntity> coverageEntities = subscription.getSubscriptionCoverages()
			.stream()
			.map(snapshot -> SubscriptionCoverageEntity.fromDomain(snapshot, newSubscriptionId))
			.collect(Collectors.toList());

		if (!coverageEntities.isEmpty()) {
			subscriptionCoverageJpaRepository.saveAll(coverageEntities);
		}

		return findById(newSubscriptionId).orElseThrow(() -> new RuntimeException("생성 후 Subscription 조회 실패")); // TODO:
																												// CoreException으로
																												// 변경
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
			.orElse(null); // (Cargo는 조회 실패시 null일 수 있음)

		List<SubscriptionCoverage> coverages = subscriptionCoverageJpaRepository.findBySubscriptionId(subscriptionId)
			.stream()
			.map(SubscriptionCoverageEntity::toDomain)
			.collect(Collectors.toList());

		return Optional.of(Subscription.builder()
			.id(subEntity.getId())
			.userId(subEntity.getUserId())
			.status(subEntity.getStatus())
			.policyNumber(subEntity.getPolicyNumber())
			.insurancePremium(subEntity.getInsurancePremium())
			.isSame(subEntity.getIsSame())
			.policyholderCompanyName(subEntity.getPolicyholderCompanyName())
			.policyholderCompanyCode(subEntity.getPolicyholderCompanyCode())
			.insuredCompanyName(subEntity.getInsuredCompanyName())
			.insuredCompanyCode(subEntity.getInsuredCompanyCode())
			.cargoDetail(cargoDetail)
			.subscriptionCoverages(coverages)
			.build());
	}

}
