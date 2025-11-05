package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.CargoDetailRequest;
import com.nexsol.cargo.core.domain.CreateSubscription;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@NoArgsConstructor
public class CreateSubscriptionRequest {

	@NotBlank(message = "Hs Code 는 필수 입니다.")
	private String hsCode;

	@NotNull(message = "송장가액은 필수 입니다.")
	@DecimalMin(value = "0.0", inclusive = false, message = "송장가액은 0보다 커야 합니다.")
	private BigDecimal invoiceAmount;

	@NotBlank(message = "화폐단위는 필수입니다.")
	private String currencyUnit;

	// TODO: 추후 우리은행 환율 API 붙이면 Deprecated
	@NotNull(message = "환율 금액은 필수입니다.")
	@DecimalMin(value = "0.0", inclusive = false, message = "환율 금액은 0보다 커야 합니다.")
	private BigDecimal exchangeRateAmount;

	@NotEmpty(message = "선택한 담보 코드는 필수입니다.")
	private Set<String> coverageCodes;

	// Policyholder
	@NotNull(message = "계약자/피보험자 동일 여부는 필수입니다.")
	private Boolean isSamePolicyholderAndInsured;

	@NotBlank(message = "계약자 상호명은 필수입니다.")
	private String policyholderCompanyName;

	@NotBlank(message = "계약자 사업자번호는 필수입니다.")
	private String policyholderCompanyCode;

	// Cargo Detail
	@NotNull(message = "운송 상세 정보는 필수입니다.")
	@Valid // CargoDetailRequest 내부의 validation도 함께 수행
	private CargoDetailRequest cargoDetail;

	private String insuredCompanyName;

	private String insuredCompanyCode;

	public CreateSubscription toCreateSubscription(Long userId) {
		return new CreateSubscription(userId, hsCode, invoiceAmount, currencyUnit, exchangeRateAmount, coverageCodes,
				isSamePolicyholderAndInsured, policyholderCompanyName, policyholderCompanyCode, insuredCompanyName,
				insuredCompanyCode, cargoDetail.toDomain());

	}

}
