package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.CargoDetail;
import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.domain.SubscriptionCoverage;
import com.nexsol.cargo.core.domain.SubscriptionRepository;
import com.nexsol.cargo.core.support.DomainPage;
import com.nexsol.cargo.core.support.DomainPageRequest;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCargoEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCoverageEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
			.signatureKey(subEntity.getSignatureKey())
			.signatureBase64Temp(subEntity.getSignatureBase64Temp())
			.signatureContentTypeTemp(subEntity.getSignatureContentTypeTemp())
			.cargoDetail(cargoDetail)
			.subscriptionCoverages(coverages)
			.build());
	}

	@Override
	public DomainPage<Subscription> findAllByUserId(Long userId, DomainPageRequest pageRequest) {
		// 1. Pageable 변환
		Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by("id").descending());

		// 2. Entity 조회
		Page<SubscriptionEntity> subPage = subscriptionJpaRepository.findByUserId(userId, pageable);
		List<SubscriptionEntity> entities = subPage.getContent();

		if (entities.isEmpty()) {
			return DomainPage.empty();
		}

		// 3. N+1 방지를 위한 하위 데이터 조회 (Cargo, Coverage)
		// Payment 조회 로직은 여기서 완전히 제거되었습니다.
		List<Long> subIds = entities.stream().map(SubscriptionEntity::getId).toList();

		Map<Long, SubscriptionCargoEntity> cargoMap = subscriptionCargoJpaRepository.findBySubscriptionIdIn(subIds)
			.stream()
			.collect(Collectors.toMap(SubscriptionCargoEntity::getSubscriptionId, Function.identity()));


		List<Subscription> subscriptions = entities.stream().map(entity -> {
			SubscriptionCargoEntity cargoEntity = cargoMap.get(entity.getId());
			CargoDetail cargoDetail = (cargoEntity != null) ? cargoEntity.toDomain() : null;

			return entity.toDomain().toBuilder().cargoDetail(cargoDetail).build();
		}).toList();

		return new DomainPage<>(subscriptions, subPage.getTotalElements(), subPage.getTotalPages(), subPage.getNumber(),
				subPage.hasNext());
	}

	@Override
	public DomainPage<Subscription> searchByContract(Long userId, SubscriptionSearch contract,
			DomainPageRequest pageRequest) {
		Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by("id").descending());

		Specification<SubscriptionEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(cb.equal(root.get("userId"), userId));

			if (contract.keyword() != null && !contract.keyword().isBlank()) {
				String likePattern = "%" + contract.keyword() + "%";
				predicates.add(cb.or(cb.like(root.get("policyholderCompanyName"), likePattern),
						cb.like(root.get("insuredCompanyName"), likePattern)));
			}

			if (contract.startDate() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), contract.startDate().atStartOfDay()));
			}

			if (contract.endDate() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), contract.endDate().atTime(LocalTime.MAX)));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		Page<SubscriptionEntity> subPage = subscriptionJpaRepository.findAll(spec, pageable);
		List<SubscriptionEntity> entities = subPage.getContent();

		if (entities.isEmpty()) {
			return DomainPage.empty();
		}

		List<Long> subIds = entities.stream().map(SubscriptionEntity::getId).toList();

		Map<Long, SubscriptionCargoEntity> cargoMap = subscriptionCargoJpaRepository.findBySubscriptionIdIn(subIds)
			.stream()
			.collect(Collectors.toMap(SubscriptionCargoEntity::getSubscriptionId, Function.identity()));

		List<Subscription> subscriptions = entities.stream().map(entity -> {
			SubscriptionCargoEntity cargoEntity = cargoMap.get(entity.getId());
			CargoDetail cargoDetail = (cargoEntity != null) ? cargoEntity.toDomain() : null;

			return entity.toDomain().toBuilder().cargoDetail(cargoDetail).build();
		}).toList();

		return new DomainPage<>(subscriptions, subPage.getTotalElements(), subPage.getTotalPages(), subPage.getNumber(),
				subPage.hasNext());
	}

}