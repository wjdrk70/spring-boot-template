package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.PaymentDetail;
import com.nexsol.cargo.core.domain.PaymentRepository;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import com.nexsol.cargo.storage.db.core.entity.PaymentDetailEntity;
import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentDetailJpaRepository paymentDetailJpaRepository;

	private final SubscriptionJpaRepository subscriptionJpaRepository;

	private final EntityManager entityManager;

	@Override
	public PaymentDetail save(PaymentDetail paymentDetail) {
		SubscriptionEntity subscriptionEntity = subscriptionJpaRepository.findById(paymentDetail.subscriptionId())
			.orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));

		if (!entityManager.contains(subscriptionEntity)) {
			subscriptionEntity = entityManager.merge(subscriptionEntity);
		}

		PaymentDetailEntity entity = PaymentDetailEntity.fromDomain(paymentDetail, subscriptionEntity);

		PaymentDetailEntity savedEntity = paymentDetailJpaRepository.save(entity);

		return savedEntity.toDomain();
	}

}
