package com.nexsol.cargo.core.api.controller.v1;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreatePaymentRequest {
    @NotNull(message = "청약 ID는 필수입니다.")
    private Long subscriptionId;

    @NotNull(message = "환율 금액은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "환율 금액은 0보다 커야 합니다.")
    private BigDecimal exchangeRateAmount;
}