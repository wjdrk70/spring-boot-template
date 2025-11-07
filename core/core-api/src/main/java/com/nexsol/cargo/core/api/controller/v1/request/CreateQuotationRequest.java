package com.nexsol.cargo.core.api.controller.v1.request;

import com.nexsol.cargo.core.domain.CreateQuotation;
import com.nexsol.cargo.core.enums.ConveyanceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CreateQuotationRequest {

	// (ocr.png) 금융 정보
	@NotBlank(message = "HS Code 는 필수 입니다.")
	private String hsCode;

	@NotNull(message = "송장가액은 필수 입니다.")
	@DecimalMin(value = "0.0", inclusive = false, message = "송장가액은 0보다 커야 합니다.")
	private BigDecimal invoiceAmount;

	@NotBlank(message = "화폐단위는 필수입니다.")
	private String currencyUnit;

	@NotNull(message = "환율 금액은 필수입니다.")
	@DecimalMin(value = "0.0", inclusive = false, message = "환율 금액은 0보다 커야 합니다.")
	private BigDecimal exchangeRateAmount;

	// (ocr.png) 운송 상세 정보
	@NotBlank(message = "Ref No는 필수입니다.")
	private String refNo;

	@NotBlank(message = "B/L No는 필수입니다.")
	private String blNo;

	@NotNull(message = "출발일자는 필수입니다.")
	private LocalDate outboundDate;

	@NotBlank(message = "출발지는 필수입니다.")
	private String origin;

	@NotBlank(message = "도착지는 필수입니다.")
	private String destination;

	@NotNull(message = "운송용구는 필수입니다.")
	private ConveyanceType conveyance;

	@NotBlank(message = "포장 구분은 필수입니다.")
	private String packingType;

	@NotBlank(message = "품목 상세는 필수입니다.")
	private String cargoItemName;

	/**
	 * API DTO를 '가견적 생성' 도메인 Command DTO로 변환
	 */
	public CreateQuotation toCreateQuotation(Long userId) {
		return CreateQuotation.builder()
			.blNo(this.blNo)
			.conveyance(this.conveyance)
			.currencyUnit(this.currencyUnit)
			.destination(this.destination)
			.exchangeRateAmount(this.exchangeRateAmount)
			.hsCode(this.hsCode)
			.invoiceAmount(this.invoiceAmount)
			.origin(this.origin)
			.packingType(this.packingType)
			.refNo(this.refNo)
			.cargoItemName(this.cargoItemName)
			.outboundDate(this.outboundDate)
			.userId(userId)
			.build();
	}

}
