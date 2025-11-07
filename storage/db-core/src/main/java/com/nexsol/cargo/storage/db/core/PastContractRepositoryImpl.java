package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.PastContractCoverage;
import com.nexsol.cargo.core.domain.PastContractRepository;
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
public class PastContractRepositoryImpl implements PastContractRepository {

	private final SubscriptionCargoJpaRepository subscriptionCargoJpaRepository;

	private final SubscriptionCoverageJpaRepository subscriptionCoverageJpaRepository;

	@Override
	public List<PastContractCoverage> findByHsCode(String hsCode) {
		List<Long> subscriptionIds = subscriptionCargoJpaRepository.findByHsCode(hsCode)
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

		return coverageSetsMap.values().stream().map(PastContractCoverage::new).collect(Collectors.toList());
	}

}
