package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionProcessor {

	private final SubscriptionRepository subscriptionRepository;

	private final SubscriptionSnapshotCreator snapshotCreator;

	public Subscription createSubscription(CreateSubscription creation, BigDecimal premium) {
		List<CoverageSnapshot> snapshots = snapshotCreator.create(creation.coverageCodes());

		if (snapshots == null) {
			snapshots = List.of(); // ⬅️
		}

		Subscription subscription = Subscription.builder()
			.hsCode(creation.hsCode())
			.invoiceAmount(creation.invoiceAmount())
			.currencyUnit(creation.currencyUnit())
			.id(null)
			.insurancePremium(premium)
			.insuredCompanyCode(creation.insuredCompanyCode())
			.userId(creation.userId())
			.insuredCompanyName(creation.insuredCompanyName())
			.policyholderCompanyName(creation.policyholderCompanyName())
			.isSame(creation.isSame())
			.insuredCompanyCode(creation.insuredCompanyCode())
			.policyholderCompanyCode(creation.policyholderCompanyCode())
			.status(SubscriptionStatus.PAYMENT_PENDING)
			.cargoDetail(creation.cargoDetail())
			.snapshots(snapshots)
			.build();

		return subscriptionRepository.save(subscription);
	}

}
