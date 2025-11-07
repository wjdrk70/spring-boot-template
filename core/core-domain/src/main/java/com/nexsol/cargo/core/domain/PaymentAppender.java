package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentAppender {

	private final PaymentRepository paymentRepository;

	public Payment appendReady(Long subscriptionId, BigDecimal amount) {

		Payment payment = Payment.createReady(subscriptionId, amount);

		return paymentRepository.save(payment);
	}

}
