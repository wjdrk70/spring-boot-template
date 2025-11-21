package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

	@Column(name = "signature_key", length = 255)
	private String signatureKey;

	@Column(name = "manager_name", length = 100)

	private String managerName;

	@Column(name = "manager_phone", length = 20)
	private String managerPhone;

	@Column(name = "manager_email", length = 100)
	private String managerEmail;

	@Lob
	@Column(name = "signature_base64_temp")
	private String signatureBase64Temp;

	@Column(name = "signature_content_type_temp", length = 50)
	private String signatureContentTypeTemp;

	@Column(name = "canceled_at")
	private LocalDateTime canceledAt;

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
		entity.signatureKey = domain.getSignatureKey();
		entity.policyNumber = domain.getPolicyNumber();
		entity.managerName = domain.getManagerName();
		entity.managerPhone = domain.getManagerPhone();
		entity.managerEmail = domain.getManagerEmail();
		entity.canceledAt = domain.getCanceledAt();

		return entity;
	}

	public void updateFromDomain(Subscription domain) {

		this.status = domain.getStatus();
		this.signatureKey = domain.getSignatureKey();
		this.signatureBase64Temp = domain.getSignatureBase64Temp();
		this.signatureContentTypeTemp = domain.getSignatureContentTypeTemp();
		this.policyNumber = domain.getPolicyNumber();
		this.canceledAt = domain.getCanceledAt();
	}

	public Subscription toDomain() {
		return Subscription.builder()
			.id(this.getId())
			.userId(this.userId)
			.status(this.status)
			.policyNumber(this.policyNumber)
			.insurancePremium(this.insurancePremium)
			.isSame(this.isSame)
			.policyholderCompanyName(this.policyholderCompanyName)
			.policyholderCompanyCode(this.policyholderCompanyCode)
			.insuredCompanyName(this.insuredCompanyName)
			.insuredCompanyCode(this.insuredCompanyCode)
			.signatureKey(this.signatureKey)
			.signatureBase64Temp(this.signatureBase64Temp)
			.signatureContentTypeTemp(this.signatureContentTypeTemp)
			.managerName(this.managerName)
			.managerPhone(this.managerPhone)
			.managerEmail(this.managerEmail)
			.canceledAt(this.canceledAt)
			.createdAt(this.getCreatedAt())
			.build();
	}

}