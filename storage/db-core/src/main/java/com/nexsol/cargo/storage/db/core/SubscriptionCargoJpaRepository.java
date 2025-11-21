package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.SubscriptionCargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionCargoJpaRepository extends JpaRepository<SubscriptionCargoEntity, Long> {

	List<SubscriptionCargoEntity> findByHsCode(String hsCode);

	List<SubscriptionCargoEntity> findByHsCodeStartingWith(String middleCode);

	List<SubscriptionCargoEntity> findBySubscriptionIdIn(List<Long> subscriptionIds);

}