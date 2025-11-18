package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.SubscriptionCoverageRepository;
import com.nexsol.cargo.core.domain.SubscriptionCoverageSet;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCargoEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionCoverageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SubscriptionCoverageRepositoryImpl implements SubscriptionCoverageRepository {

	private final SubscriptionCargoJpaRepository subscriptionCargoJpaRepository;

	private final SubscriptionCoverageJpaRepository subscriptionCoverageJpaRepository;

	@Override
	public List<SubscriptionCoverageSet> findByMiddleCode(String middleCode) {

		List<Long> subscriptionIds = subscriptionCargoJpaRepository.findByHsCodeStartingWith(middleCode)
			.stream()
			.map(SubscriptionCargoEntity::getSubscriptionId)
			.distinct()
			.toList();

		if (subscriptionIds.isEmpty()) {
			return List.of();
		}

		List<SubscriptionCoverageEntity> allCoverages = subscriptionCoverageJpaRepository
			.findBySubscriptionIdIn(subscriptionIds);

		Map<Long, Set<String>> coverageSetsMap = allCoverages.stream()
			.collect(Collectors.groupingBy(SubscriptionCoverageEntity::getSubscriptionId,
					Collectors.mapping(SubscriptionCoverageEntity::getConditionCode, Collectors.toSet())));

		return coverageSetsMap.values().stream().map(SubscriptionCoverageSet::new).collect(Collectors.toList());
	}

}
