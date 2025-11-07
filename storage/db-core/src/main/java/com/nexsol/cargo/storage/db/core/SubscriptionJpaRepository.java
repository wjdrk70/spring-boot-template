package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {

}
