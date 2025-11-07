package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.Quotation;
import com.nexsol.cargo.core.enums.ConveyanceType;
import com.nexsol.cargo.core.enums.QuotationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Table(name = "quotation")
@Entity
@NoArgsConstructor
public class QuotationEntity {

	@Id // ⬅️ [추가]
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "quotation_key", nullable = false, unique = true)
	private String quotationKey;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private QuotationStatus status;

	@Column(name = "hs_code", nullable = false)
	private String hsCode;

	@Column(name = "invoice_amount", nullable = false)
	private BigDecimal invoiceAmount;

	@Column(name = "currency_unit", nullable = false)
	private String currencyUnit;

	@Column(name = "exchange_rate_amount", nullable = false)
	private BigDecimal exchangeRateAmount;

	@Column(name = "ref_no")
	private String refNo;

	@Column(name = "bl_no")
	private String blNo;

	@Column(name = "outbound_date")
	private LocalDate outboundDate;

	@Column
	private String origin;

	@Column
	private String destination;

	@Enumerated(EnumType.STRING)
	@Column
	private ConveyanceType conveyance;

	@Column(name = "packing_type")
	private String packingType;

	@Column(name = "cargo_item_name")
	private String cargoItemName;

	@CreationTimestamp // ⬅️ [추가]
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp // ⬅️ [추가]
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public static QuotationEntity fromDomain(Quotation domain) {
		QuotationEntity entity = new QuotationEntity();
		entity.id = domain.getId();
		entity.userId = domain.getUserId();
		entity.quotationKey = domain.getQuotationKey();
		entity.status = domain.getStatus();
		entity.hsCode = domain.getHsCode();
		entity.invoiceAmount = domain.getInvoiceAmount();
		entity.currencyUnit = domain.getCurrencyUnit();
		entity.exchangeRateAmount = domain.getExchangeRateAmount();
		entity.refNo = domain.getRefNo();
		entity.blNo = domain.getBlNo();
		entity.outboundDate = domain.getOutboundDate();
		entity.origin = domain.getOrigin();
		entity.destination = domain.getDestination();
		entity.conveyance = domain.getConveyance();
		entity.packingType = domain.getPackingType();
		entity.cargoItemName = domain.getCargoItemName();
		return entity;
	}

	public Quotation toDomain() {
		return Quotation.builder()
			.id(this.getId())
			.userId(this.userId)
			.quotationKey(this.quotationKey)
			.status(this.status)
			.hsCode(this.hsCode)
			.invoiceAmount(this.invoiceAmount)
			.currencyUnit(this.currencyUnit)
			.exchangeRateAmount(this.exchangeRateAmount)
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

}
