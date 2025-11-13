package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.VoyageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoyageJpaRepository extends JpaRepository<VoyageEntity, Long> {

}
