package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentAppender {

	private final PaymentRepository paymentRepository;

	public Payment appendReady(Long subscriptionId, BigDecimal amount, PaymentMethod paymentMethod) {

		Payment payment = paymentRepository.findBySubscriptionId(subscriptionId)
			.orElseGet(() -> Payment.createReady(subscriptionId, amount, paymentMethod));

		if (payment.getId() != null) {
			payment.resetToReady(amount, paymentMethod);
		}

		return paymentRepository.save(payment);
	}

}
