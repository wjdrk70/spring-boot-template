package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentProcessor {

	private final PaymentRepository paymentRepository;

	public PaymentDetail createReadyState(Long subscriptionId, BigDecimal amount) {

		PaymentDetail paymentDetail = PaymentDetail.builder()
			.subscriptionId(subscriptionId)
			.paymentStatus(PaymentStatus.READY)
			.paymentMethod(null)
			.cardType(null)
			.cardNumberMasked(null)
			.expiryDate(null)
			.build();

		return paymentRepository.save(paymentDetail);
	}

}
