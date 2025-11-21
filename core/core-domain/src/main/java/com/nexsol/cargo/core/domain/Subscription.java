package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

	private Long id;

	private Long userId;

	private SubscriptionStatus status;

	private String policyNumber;

	private BigDecimal insurancePremium;

	private boolean isSame;

	private String policyholderCompanyName;

	private String policyholderCompanyCode;

	private String insuredCompanyName;

	private String insuredCompanyCode;

	private CargoDetail cargoDetail;

	private String managerName;

	private String managerPhone;

	private String managerEmail;

	private String signatureKey;

	private String signatureBase64Temp;

	private String signatureContentTypeTemp;

	private LocalDateTime createdAt;

	private LocalDateTime canceledAt;

	private List<SubscriptionCoverage> subscriptionCoverages;

	public void completePayment() {
		// (방어 로직)
		if (this.status != SubscriptionStatus.PAYMENT_PENDING) {
			// TODO: 멱등성 처리 추후 상세 구현
			return;
		}
		this.status = SubscriptionStatus.PAYMENT_COMPLETE;
	}

	public void cancelPayment() {
		if (this.status != SubscriptionStatus.PAYMENT_COMPLETE) {
			return;
		}
		this.status = SubscriptionStatus.CANCEL;
		this.canceledAt = LocalDateTime.now();
	}

	public void issuePolicy(String policyNumber) {
		if (this.status != SubscriptionStatus.PAYMENT_COMPLETE) {
			// TODO: 오류처리
			return;
		}
		this.policyNumber = policyNumber;
		this.status = SubscriptionStatus.POLICY_ISSUED;
	}

	public void updateSignatureKey(String signatureKey) {
		this.signatureKey = signatureKey;
	}

	public void saveSignatureBase64Temp(String base64String, String contentType) {
		if (status != SubscriptionStatus.PAYMENT_PENDING) {
			throw new CoreException(CoreErrorType.SUBSCRIPTION_PENDING_FAILED);
		}
		this.signatureBase64Temp = base64String;

	}

	public void clearSignatureBase64Temp() {
		this.signatureBase64Temp = null;
		this.signatureContentTypeTemp = null;
	}

}
