package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.SubscriptionCargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionCargoJpaRepository extends JpaRepository<SubscriptionCargoEntity, Long> {
    // 'hsCode'로 모든 cargo 데이터를 조회 (추천 분석용)
    List<SubscriptionCargoEntity> findByHsCode(String hsCode);
}