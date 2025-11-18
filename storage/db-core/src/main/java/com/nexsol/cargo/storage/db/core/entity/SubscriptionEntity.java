package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

	@Column(name = "insurance_premium", nullable = false)
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

	@Column(name = "policy_number", length = 100)
	private String policyNumber;

	public static SubscriptionEntity fromDomain(Subscription domain) {
		SubscriptionEntity entity = new SubscriptionEntity();

		entity.userId = domain.getUserId();
		entity.status = domain.getStatus();
		entity.insurancePremium = domain.getInsurancePremium();
		entity.isSame = domain.isSame();
		entity.policyholderCompanyName = domain.getPolicyholderCompanyName();
		entity.policyholderCompanyCode = domain.getPolicyholderCompanyCode();
		entity.insuredCompanyName = domain.getInsuredCompanyName();
		entity.insuredCompanyCode = domain.getInsuredCompanyCode();
		entity.policyNumber = domain.getPolicyNumber();

		return entity;
	}

	public void updateFromDomain(Subscription domain) {

		this.status = domain.getStatus();
		this.policyNumber = domain.getPolicyNumber();

	}

	public Subscription toDomain() {
		return Subscription.builder()
			.id(this.getId()) // 💡 ID 포함
			.userId(this.userId)
			.status(this.status)
			.policyNumber(this.policyNumber)
			.insurancePremium(this.insurancePremium)
			.isSame(this.isSame)
			.policyholderCompanyName(this.policyholderCompanyName)
			.policyholderCompanyCode(this.policyholderCompanyCode)
			.insuredCompanyName(this.insuredCompanyName)
			.insuredCompanyCode(this.insuredCompanyCode)
			// (cargoDetail, subscriptionCoverages는 RepositoryImpl에서 조립)
			.build();
	}

}