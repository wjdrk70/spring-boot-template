package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.*;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import com.nexsol.cargo.core.support.DomainPage;
import com.nexsol.cargo.core.support.DomainPageRequest;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCargoEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCoverageEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

	private final EntityManager entityManager;

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
			.orElseThrow(() -> new CoreException(CoreErrorType.SUBSCRIPTION_NOR_FOUND_DATA));

		managedEntity.updateFromDomain(subscription);

		subscriptionJpaRepository.save(managedEntity);

		return findById(subscription.getId())
			.orElseThrow(() -> new CoreException(CoreErrorType.SUBSCRIPTION_NOR_FOUND_DATA));
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

		return findById(newSubscriptionId)
			.orElseThrow(() -> new CoreException(CoreErrorType.SUBSCRIPTION_NOR_FOUND_DATA));
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
	public SubscriptionSummery<Subscription> findAllByUserId(Long userId, DomainPageRequest pageRequest) {
		SubscriptionSearch emptyCondition = new SubscriptionSearch(null, null, null);

		return searchByContract(userId, emptyCondition, pageRequest);
	}

	@Override
	public SubscriptionSummery<Subscription> searchByContract(Long userId, SubscriptionSearch contract,
			DomainPageRequest pageRequest) {
		Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by("id").descending());

		Specification<SubscriptionEntity> spec = createSpec(userId, contract);

		Page<SubscriptionEntity> subPage = subscriptionJpaRepository.findAll(spec, pageable);

		BigDecimal totalPremium = calculateTotalPremium(spec);

		List<Subscription> subscriptions = mapToDomainList(subPage.getContent());

		DomainPage<Subscription> domainPage = new DomainPage<>(subscriptions, subPage.getTotalElements(),
				subPage.getTotalPages(), subPage.getNumber(), subPage.hasNext());

		return new SubscriptionSummery(domainPage, totalPremium);
	}

	private Specification<SubscriptionEntity> createSpec(Long userId, SubscriptionSearch contract) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get("userId"), userId));

			if (contract != null) { // 조건이 있을 때만 추가
				if (contract.keyword() != null && !contract.keyword().isBlank()) {
					String likePattern = "%" + contract.keyword() + "%";
					predicates.add(cb.or(cb.like(root.get("policyholderCompanyName"), likePattern),
							cb.like(root.get("insuredCompanyName"), likePattern)));
				}
				if (contract.startDate() != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), contract.startDate().atStartOfDay()));
				}
				if (contract.endDate() != null) {
					predicates
						.add(cb.lessThanOrEqualTo(root.get("createdAt"), contract.endDate().atTime(LocalTime.MAX)));
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	private List<Subscription> mapToDomainList(List<SubscriptionEntity> entities) {
		if (entities.isEmpty()) {
			return List.of();
		}

		List<Long> subIds = entities.stream().map(SubscriptionEntity::getId).toList();

		Map<Long, SubscriptionCargoEntity> cargoMap = subscriptionCargoJpaRepository.findBySubscriptionIdIn(subIds)
			.stream()
			.collect(Collectors.toMap(SubscriptionCargoEntity::getSubscriptionId, Function.identity()));

		return entities.stream().map(entity -> {
			SubscriptionCargoEntity cargoEntity = cargoMap.get(entity.getId());
			CargoDetail cargoDetail = (cargoEntity != null) ? cargoEntity.toDomain() : null;

			return entity.toDomain().toBuilder().cargoDetail(cargoDetail).build();
		}).toList();
	}

	private BigDecimal calculateTotalPremium(Specification<SubscriptionEntity> spec) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
		Root<SubscriptionEntity> root = query.from(SubscriptionEntity.class);

		query.select(cb.sum(root.get("insurancePremium")));

		if (spec != null) {
			Predicate predicate = spec.toPredicate(root, query, cb);
			if (predicate != null) {
				query.where(predicate);
			}
		}

		BigDecimal result = entityManager.createQuery(query).getSingleResult();
		return result != null ? result : BigDecimal.ZERO;
	}

}