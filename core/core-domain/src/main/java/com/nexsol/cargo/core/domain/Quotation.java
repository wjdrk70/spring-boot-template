package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConveyanceType;
import com.nexsol.cargo.core.enums.QuotationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {

	private Long id;

	private Long userId;

	private String quotationKey; // 클라이언트에게 반환할 고유 Key

	private QuotationStatus status;

	// OCR 데이터
	private String hsCode;

	private BigDecimal invoiceAmount;

	private String currencyUnit;

	private BigDecimal exchangeRateAmount;

	private String refNo;

	private String blNo;

	private LocalDate outboundDate;

	private String origin;

	private String destination;

	private ConveyanceType conveyance;

	private String packingType;

	private String cargoItemName;

	public static Quotation createPending(CreateQuotation creation, String quotationKey) {
		return Quotation.builder()
			.id(null)
			.userId(creation.userId())
			.quotationKey(quotationKey)
			.status(QuotationStatus.PENDING)
			.hsCode(creation.hsCode())
			.invoiceAmount(creation.invoiceAmount())
			.currencyUnit(creation.currencyUnit())
			.exchangeRateAmount(creation.exchangeRateAmount())
			.refNo(creation.refNo())
			.blNo(creation.blNo())
			.outboundDate(creation.outboundDate())
			.origin(creation.origin())
			.destination(creation.destination())
			.conveyance(creation.conveyance())
			.packingType(creation.packingType())
			.cargoItemName(creation.cargoItemName())
			.build();
	}

	public CargoDetail toCargoDetail() {
		return CargoDetail.builder()
			.hsCode(this.hsCode)
			.invoiceAmount(this.invoiceAmount)
			.currencyUnit(this.currencyUnit)
			.refNo(this.refNo)
			.blNo(this.blNo)
			.outboundDate(this.outboundDate)
			.origin(this.origin)
			.destination(this.destination)
			.conveyance(this.conveyance)
			.packingType(this.packingType)
			.cargoItemName(this.cargoItemName)
			.build();
	}

	public void subscribe() {
		this.status = QuotationStatus.SUBSCRIBED;
	}

}
