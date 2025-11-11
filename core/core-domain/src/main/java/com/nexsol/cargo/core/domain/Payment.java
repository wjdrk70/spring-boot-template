package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentMethod;
import com.nexsol.cargo.core.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

	private Long id;

	private String authCode;

	private Long subscriptionId;

	private BigDecimal insurancePremium;

	private PaymentStatus paymentStatus;

	private PaymentMethod paymentMethod;

	private String cardType;

	private String cardNumberMasked;

	private String expiryDate;

	private String externalPaymentKey; // PG사의 거래 ID (TID) 또는 승인 번호 (AuthCode)

	public static Payment createReady(Long subscriptionId, BigDecimal insurancePremium, PaymentMethod paymentMethod) {
		return Payment.builder()
			.id(null)
			.subscriptionId(subscriptionId)
			.insurancePremium(insurancePremium)
			.paymentStatus(PaymentStatus.READY)
			.paymentMethod(paymentMethod)
			.build();
	}

	public void success(String tid,String authCode, String cardCode) {
		if (this.paymentStatus != PaymentStatus.READY) {
			return;
		}
		this.paymentStatus = PaymentStatus.SUCCESS;
		this.externalPaymentKey = tid;
		this.authCode = authCode;
		this.cardType = cardCode;

	}

	public void cancel() {
		if (this.paymentStatus != PaymentStatus.SUCCESS) {
			// TODO: 이미 취소되었거나 성공한 결제가 아닐 경우 예외 처리
			return;
		}
		this.paymentStatus = PaymentStatus.CANCEL;
	}

}
