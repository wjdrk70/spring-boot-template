package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.PaymentReadyResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreatePaymentResponse {

	private String moid;

	private String mid;

	private String amt;

	private String ediDate;

	private String signData;

	public static CreatePaymentResponse fromDomain(PaymentReadyResult result) {
		return CreatePaymentResponse.builder()
			.moid(result.moid())
			.mid(result.mid())
			.amt(result.amt())
			.ediDate(result.ediDate())
			.signData(result.signData())
			.build();
	}

}