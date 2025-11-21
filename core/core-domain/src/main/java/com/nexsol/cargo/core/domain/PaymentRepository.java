package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaymentRepository {

	Payment save(Payment payment);

	Optional<Payment> findById(Long paymentId);

	Optional<Payment> findBySubscriptionId(Long subscriptionId);

	Optional<Payment> findByTidAndStatus(String tid, PaymentStatus status);

	Map<Long, Payment> findAllBySubscriptionIdIn(List<Long> subscriptionIds);

}
