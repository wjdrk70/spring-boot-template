package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.SubscriptionResult;
import com.nexsol.cargo.core.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateSubscriptionResponse {

	private Long subscriptionId;

	private BigDecimal insurancePremium;

	private PaymentStatus paymentStatus;

	public static CreateSubscriptionResponse of(SubscriptionResult subscriptionResult) {
		return CreateSubscriptionResponse.builder()
			.subscriptionId(subscriptionResult.subscriptionId())
			.insurancePremium(subscriptionResult.insurancePremium())
			.build();
	}

}
