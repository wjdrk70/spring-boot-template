package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.CoverageOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CoverageOptionJpaRepository extends JpaRepository<CoverageOptionEntity, Long> {

	List<CoverageOptionEntity> findByCodeIn(Set<String> codes);

}
