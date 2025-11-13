package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.BaseRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRateJpaRepository extends JpaRepository<BaseRateEntity, Long> {

}
