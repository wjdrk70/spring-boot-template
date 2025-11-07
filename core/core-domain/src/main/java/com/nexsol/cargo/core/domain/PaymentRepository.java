package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentStatus;

import java.util.Optional;

public interface PaymentRepository {

	Payment save(Payment payment);

	Optional<Payment> findById(Long paymentId);

	Optional<Payment> findByTidAndStatus(String tid, PaymentStatus status);

}
