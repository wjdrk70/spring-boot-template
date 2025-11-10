package com.nexsol.cargo.core.domain;

import java.math.BigDecimal;

public record PaymentReadyResult(String moid, // 주문번호 (우리 DB의 payment.id)
		String mid, // 상점 ID
		String amt, // 결제 금액 (String, "1000")
		String ediDate, // YYYYMMDDHHMMSS
		String signData // 해시 서명

) {
	public PaymentReadyResult(String moid, BigDecimal amount, String signData) {
		this(moid, null, amount.toPlainString(), null, signData);
	}
}