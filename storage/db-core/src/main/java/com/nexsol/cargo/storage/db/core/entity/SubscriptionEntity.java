package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Table(name = "subscription")
@Entity
@NoArgsConstructor
public class SubscriptionEntity extends BaseEntity {

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private SubscriptionStatus status;

	@Column(name = "insurance_premium")
	private BigDecimal insurancePremium;

	@Column(name = "is_same")
	private Boolean isSame;

	@Column(name = "policyholder_company_name")
	private String policyholderCompanyName;

	@Column(name = "policyholder_company_code")
	private String policyholderCompanyCode;

	@Column(name = "insured_company_name")
	private String insuredCompanyName;

	@Column(name = "insured_company_code")
	private String insuredCompanyCode;

	@Column(name = "hs_code")
	private String hsCode;

	@Column(name = "invoice_amount")
	private BigDecimal invoiceAmount;

	@Column(name = "currency_unit")
	private String currencyUnit;

	@OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubscriptionSnapshotEntity> snapshots;

	@OneToOne(mappedBy = "subscription", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private SubscriptionCargoDetailEntity cargoDetail;

	public static SubscriptionEntity fromDomain(Subscription domain) {
		SubscriptionEntity entity = new SubscriptionEntity();

		entity.userId = domain.userId();
		entity.status = domain.status();
		entity.insurancePremium = domain.insurancePremium();
		entity.isSame = domain.isSame();
		entity.policyholderCompanyName = domain.policyholderCompanyName();
		entity.policyholderCompanyCode = domain.policyholderCompanyCode();
		entity.insuredCompanyName = domain.insuredCompanyName();
		entity.insuredCompanyCode = domain.insuredCompanyCode();
		entity.hsCode = domain.hsCode();
		entity.invoiceAmount = domain.invoiceAmount();
		entity.currencyUnit = domain.currencyUnit();

		entity.snapshots = domain.snapshots()
			.stream()
			.map(snapshot -> SubscriptionSnapshotEntity.fromDomain(snapshot, entity))
			.collect(Collectors.toList());

		entity.cargoDetail = SubscriptionCargoDetailEntity.fromDomain(domain.cargoDetail(), entity);

		return entity;
	}

	public Subscription toDomain() {
		return new Subscription(this.getId(), this.userId, this.status, this.insurancePremium, this.invoiceAmount,
				this.currencyUnit, this.hsCode, this.isSame, this.policyholderCompanyName, this.policyholderCompanyCode,
				this.insuredCompanyName, this.insuredCompanyCode,
				(this.cargoDetail != null) ? this.cargoDetail.toDomain() : null,
				(this.snapshots != null)
						? this.snapshots.stream().map(SubscriptionSnapshotEntity::toDomain).collect(Collectors.toList())
						: List.of());
	}

}