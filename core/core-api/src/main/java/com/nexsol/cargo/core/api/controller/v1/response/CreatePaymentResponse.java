package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.PaymentReadyResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreatePaymentResponse {

	private String tid;

	private BigDecimal amount;

	private String signData;

	// private String mid; (공통 값이면 클라이언트가 알 수도 있음)

	public static CreatePaymentResponse fromDomain(PaymentReadyResult result) {
		return CreatePaymentResponse.builder()
			.tid(result.tid())
			.amount(result.amount())
			.signData(result.signData())
			.build();
	}

}