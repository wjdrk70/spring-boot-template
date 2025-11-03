package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.CoverageRiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CoverageRiderJpaRepository extends JpaRepository<CoverageRiderEntity,Long> {
    Optional<CoverageRiderEntity> findByCodeIn(Set<String> codes);
}
