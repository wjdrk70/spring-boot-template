package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentReader {

	private final PaymentRepository paymentRepository;

	public Map<Long, PaymentMethod> readMethodsBySubscriptionIds(List<Long> subscriptionIds) {
		if (subscriptionIds.isEmpty()) {
			return Map.of();
		}
		return paymentRepository.findAllBySubscriptionIdIn(subscriptionIds)
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue().getPaymentMethod() != null)
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getPaymentMethod()));
	}

}