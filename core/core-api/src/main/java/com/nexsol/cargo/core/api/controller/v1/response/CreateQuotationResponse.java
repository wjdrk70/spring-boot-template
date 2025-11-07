package com.nexsol.cargo.core.api.controller.v1.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateQuotationResponse {

	private final String quotationKey;

	public static CreateQuotationResponse from(String quotationKey) {
		return new CreateQuotationResponse(quotationKey);
	}

}
