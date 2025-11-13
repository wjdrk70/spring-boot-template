package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.CargoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoItemJpaRepository extends JpaRepository<CargoItemEntity, String> {

}
