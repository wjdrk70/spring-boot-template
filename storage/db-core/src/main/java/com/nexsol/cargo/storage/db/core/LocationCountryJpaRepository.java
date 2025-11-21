package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.LocationCountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationCountryJpaRepository extends JpaRepository<LocationCountryEntity, Long> {

	Optional<LocationCountryEntity> findByCityNameIgnoreCase(String cityName);

	Optional<LocationCountryEntity> findFirstByCountryName(String countryName);

}