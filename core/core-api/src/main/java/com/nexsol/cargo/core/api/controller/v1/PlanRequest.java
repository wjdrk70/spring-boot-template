package com.nexsol.cargo.core.api.controller.v1;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PlanRequest(@NotBlank(message = "HS Code는 필수입니다.") String hsCode,

		@NotNull(message = "송장가액은 필수입니다.") @DecimalMin(value = "0.0", inclusive = false,
				message = "송장가액은 0보다 커야 합니다.") BigDecimal invoiceAmount,

		@NotBlank(message = "화폐단위는 필수입니다.") String currencyUnit, // [수정] 화폐단위 (예: USD)

		@NotNull(message = "환율 금액은 필수입니다.") @DecimalMin(value = "0.0", inclusive = false,
				message = "환율 금액은 0보다 커야 합니다.") BigDecimal exchangeRateAmount // [추가] 환율
																				// 금액 (예:
																				// 1404.40)

) {

}
