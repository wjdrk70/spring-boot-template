package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionProcessor {

	private final SubscriptionRepository subscriptionRepository;

	private final SubscriptionSnapshotCreator snapshotCreator;

	public Subscription createSubscription(CreateSubscription creation) {
		List<CoverageSnapshot> snapshots = snapshotCreator.create(creation.coverageCodes());

		if (snapshots == null) {
			snapshots = List.of();
		}

		Subscription subscription = Subscription.builder()
                .id(null)
                .userId(creation.userId())
                .status(SubscriptionStatus.PAYMENT_PENDING)
                .isSame(creation.isSame())
                .policyholderCompanyName(creation.policyholderCompanyName())
                .policyholderCompanyCode(creation.policyholderCompanyCode())
                .insuredCompanyName(creation.insuredCompanyName())
                .insuredCompanyCode(creation.insuredCompanyCode())
                .cargoDetail(creation.cargoDetail())
                .snapshots(snapshots)
                .build();

		return subscriptionRepository.save(subscription);
	}

}
