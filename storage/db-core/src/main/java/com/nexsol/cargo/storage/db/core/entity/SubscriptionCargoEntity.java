package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.CargoDetail;
import com.nexsol.cargo.core.enums.ConveyanceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Table(name = "subscription_cargo")
@Entity
@NoArgsConstructor
public class SubscriptionCargoEntity {

	@Id
	@Column(name = "subscription_id")
	private Long subscriptionId;

	@Column(name = "hs_code", nullable = false)
	private String hsCode;

	@Column(name = "invoice_amount", nullable = false)
	private BigDecimal invoiceAmount;

	@Column(name = "currency_unit", nullable = false)
	private String currencyUnit;

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

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public static SubscriptionCargoEntity fromDomain(CargoDetail domain, Long newSubscriptionId) {
		SubscriptionCargoEntity entity = new SubscriptionCargoEntity();
		entity.subscriptionId = newSubscriptionId;
		entity.hsCode = domain.hsCode();
		entity.invoiceAmount = domain.invoiceAmount();
		entity.currencyUnit = domain.currencyUnit();
		entity.refNo = domain.refNo();
		entity.blNo = domain.blNo();
		entity.outboundDate = domain.outboundDate();
		entity.origin = domain.origin();
		entity.destination = domain.destination();
		entity.conveyance = domain.conveyance();
		entity.packingType = domain.packingType();
		entity.cargoItemName = domain.cargoItemName();
		return entity;
	}

	public CargoDetail toDomain() {
		return new CargoDetail(this.hsCode, this.invoiceAmount, this.currencyUnit, this.refNo, this.blNo,
				this.outboundDate, this.origin, this.destination, this.conveyance, this.packingType,
				this.cargoItemName);
	}

}
