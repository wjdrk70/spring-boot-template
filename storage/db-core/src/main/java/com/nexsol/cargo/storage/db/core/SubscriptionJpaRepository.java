package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriptionJpaRepository
		extends JpaRepository<SubscriptionEntity, Long>, JpaSpecificationExecutor<SubscriptionEntity> {

	Page<SubscriptionEntity> findByUserId(Long userId, Pageable pageable);

}
