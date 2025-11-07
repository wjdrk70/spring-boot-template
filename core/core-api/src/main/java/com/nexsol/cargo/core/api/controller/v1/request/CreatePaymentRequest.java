package com.nexsol.cargo.core.api.controller.v1.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePaymentRequest {

	@NotNull(message = "청약 ID는 필수입니다.")
	private Long subscriptionId;

}