package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.BaseRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseRateJpaRepository extends JpaRepository<BaseRateEntity, Long> {

	Optional<BaseRateEntity> findByMiddleCodeAndBaseCoverageCodeAndVoyageCode(String middleCode,
			String baseCoverageCode, String voyageCode);

}
