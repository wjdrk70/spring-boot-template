package com.nexsol.cargo.core.domain;

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

	private Long subscriptionId;

	private BigDecimal insurancePremium;

	private PaymentStatus paymentStatus;

	// PG 연동 상세
	private String paymentMethod;

	private String cardType;

	private String cardNumberMasked;

	private String expiryDate;

	private String externalPaymentKey; // PG사의 거래 ID (TID) 또는 승인 번호 (AuthCode)

	public static Payment createReady(Long subscriptionId, BigDecimal insurancePremium) {
		return Payment.builder()
			.id(null)
			.subscriptionId(subscriptionId)
			.insurancePremium(insurancePremium)
			.paymentStatus(PaymentStatus.READY)
			.build();
	}

	public void success(String authCode, String cardCode) {
		// (방어 로직: READY 상태일 때만 SUCCESS로 변경 가능)
		if (this.paymentStatus != PaymentStatus.READY) {
			// TODO: 이미 처리된 결제는 로그만 남김 (멱등성) 추후 수정
			return;
		}

		this.paymentStatus = PaymentStatus.SUCCESS;
		this.externalPaymentKey = authCode; // PG 승인 번호
		this.cardType = cardCode; // PG에서 받은 카드 코드 (e.g., "01")

	}

}
