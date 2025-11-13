package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.CoverageBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CoverageBaseJpaRepository extends JpaRepository<CoverageBaseEntity, Long> {

	Optional<CoverageBaseEntity> findByCodeIn(Set<String> codes);

}
