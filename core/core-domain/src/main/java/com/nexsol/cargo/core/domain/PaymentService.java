package com.nexsol.cargo.core.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentProcessor paymentProcessor;

	@Transactional
	public PaymentDetail readyPayment(Long subscriptionId, BigDecimal amount) {
		return paymentProcessor.createReadyState(subscriptionId, amount);
	}

	// TODO: PG 연동을 위한 결제(승인,취소) 추가

}
