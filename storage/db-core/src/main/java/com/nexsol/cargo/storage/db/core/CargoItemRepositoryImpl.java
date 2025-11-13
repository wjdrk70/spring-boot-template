package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.CargoItemRepository;
import com.nexsol.cargo.storage.db.core.entity.CargoItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CargoItemRepositoryImpl implements CargoItemRepository {

	private final CargoItemJpaRepository cargoItemJpaRepository;

	@Override
	public Optional<String> findMiddleCodeByHsCode(String hsCode) {
		return cargoItemJpaRepository.findById(hsCode).map(CargoItemEntity::getMiddleCode);
	}

}
