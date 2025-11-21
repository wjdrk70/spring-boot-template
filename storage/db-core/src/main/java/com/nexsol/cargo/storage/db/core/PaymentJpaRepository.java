package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

	List<PaymentEntity> findBySubscriptionIdIn(List<Long> subscriptionIds);

	Optional<PaymentEntity> findBySubscriptionId(Long subscriptionId);

}
