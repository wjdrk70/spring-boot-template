package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.Payment;
import com.nexsol.cargo.core.domain.PaymentRepository;
import com.nexsol.cargo.core.enums.PaymentStatus;
import com.nexsol.cargo.storage.db.core.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJpaRepository paymentJpaRepository;

	@Override
	public Payment save(Payment payment) {

		PaymentEntity entity = PaymentEntity.fromDomain(payment);

		PaymentEntity savedEntity = paymentJpaRepository.save(entity);

		return savedEntity.toDomain();
	}

	@Override
	public Optional<Payment> findById(Long paymentId) {
		return paymentJpaRepository.findById(paymentId).map(PaymentEntity::toDomain);
	}

	@Override
	public Optional<Payment> findBySubscriptionId(Long subscriptionId) {
		return paymentJpaRepository.findBySubscriptionId(subscriptionId) // JPA 메서드 필요
																			// (Optional<PaymentEntity>)
			.map(PaymentEntity::toDomain);
	}

	@Override
	public Optional<Payment> findByTidAndStatus(String tid, PaymentStatus status) {
		try {
			Long paymentId = Long.parseLong(tid);
			return paymentJpaRepository.findById(paymentId)
				.filter(payment -> payment.getPaymentStatus() == status)
				.map(PaymentEntity::toDomain);
		}
		catch (NumberFormatException e) {
			return Optional.empty(); // (TID가 Long이 아닌 경우)
		}
	}

	@Override
	public Map<Long, Payment> findAllBySubscriptionIdIn(List<Long> subscriptionIds) {
		if (subscriptionIds == null || subscriptionIds.isEmpty()) {
			return Map.of();
		}

		return paymentJpaRepository.findBySubscriptionIdIn(subscriptionIds)
			.stream()
			.map(PaymentEntity::toDomain)
			.collect(Collectors.toMap(Payment::getSubscriptionId, Function.identity(), (p1, p2) -> p1));
	}

}
