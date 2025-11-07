package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.SubscriptionCoverageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionCoverageJpaRepository extends JpaRepository<SubscriptionCoverageEntity, Long> {

	List<SubscriptionCoverageEntity> findBySubscriptionIdIn(List<Long> subscriptionIds);

	List<SubscriptionCoverageEntity> findBySubscriptionId(Long subscriptionId);

}
