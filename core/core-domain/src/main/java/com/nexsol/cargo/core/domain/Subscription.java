package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

	private Long id;

	private Long userId;

	private SubscriptionStatus status;

	private BigDecimal insurancePremium;

	private boolean isSame;

	private String policyholderCompanyName;

	private String policyholderCompanyCode;

	private String insuredCompanyName;

	private String insuredCompanyCode;

	private CargoDetail cargoDetail;

	private List<SubscriptionCoverage> subscriptionCoverages;

	public void completePayment() {
		// (방어 로직)
		if (this.status != SubscriptionStatus.PAYMENT_PENDING) {
			// TODO: 멱등성 처리 추후 상세 구현
			return;
		}
		this.status = SubscriptionStatus.PAYMENT_COMPLETE;
	}

	public void cancelPayment() {
		if (this.status != SubscriptionStatus.PAYMENT_COMPLETE) {
			return;
		}
		this.status = SubscriptionStatus.CANCEL;
	}

}
