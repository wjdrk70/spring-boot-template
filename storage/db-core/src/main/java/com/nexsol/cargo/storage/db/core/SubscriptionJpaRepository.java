package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    @Query("SELECT DISTINCT s FROM SubscriptionEntity s " +
            "LEFT JOIN FETCH s.snapshots " +
            "WHERE s.hsCode = :hsCode")
    List<SubscriptionEntity> findByHsCodeWithSnapshots(@Param("hsCode") String hsCode);
}
