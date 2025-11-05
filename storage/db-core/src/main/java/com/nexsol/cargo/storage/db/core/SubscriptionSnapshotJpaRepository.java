package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.SubscriptionSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionSnapshotJpaRepository extends JpaRepository<SubscriptionSnapshotEntity, Long> {

}
