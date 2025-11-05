package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.PastContractCoverage;
import com.nexsol.cargo.core.domain.PastContractRepository;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionSnapshotEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PastContractRepositoryImpl implements PastContractRepository {

	private final SubscriptionJpaRepository subscriptionJpaRepository;

	@Override
	public List<PastContractCoverage> findByHsCode(String hsCode) {
		List<SubscriptionEntity> subscriptions = subscriptionJpaRepository.findByHsCodeWithSnapshots(hsCode);

		// 2. 각 청약(Subscription)을 순회하며 담보 코드 Set으로 변환
		return subscriptions.stream().map(subscription -> {
			// 3. 해당 청약에 묶인 스냅샷에서 '담보 코드'만 추출하여 Set으로 만듦
			Set<String> coverageCodes = subscription.getSnapshots()
				.stream()
				.map(SubscriptionSnapshotEntity::getConditionCode)
				.collect(Collectors.toSet());

			return new PastContractCoverage(coverageCodes);
		}).collect(Collectors.toList());
	}

}
