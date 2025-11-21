package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.VoyageRepository;
import com.nexsol.cargo.storage.db.core.entity.LocationCountryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VoyageRepositoryImpl implements VoyageRepository {

	private final VoyageJpaRepository voyageJpaRepository;

	private final LocationCountryJpaRepository locationCountryJpaRepository;

	@Override
	public Optional<String> findVoyageCodeByOrigin(String origin) {
		if (origin == null || origin.isBlank()) {
			return Optional.empty();
		}

		// origin 파싱 (예: "HO CHI MINH, VIETNAM")
		String[] parts = origin.split(",");
		String city = (parts.length > 0) ? parts[0].trim() : "";
		String country = (parts.length > 1) ? parts[1].trim() : "";

		// 1. 도시명으로 조회 (IgnoreCase 적용)
		if (!city.isEmpty()) {
			// Optional<LocationCountryEntity> -> map -> Optional<String>
			Optional<String> voyageCodeByCity = locationCountryJpaRepository.findByCityNameIgnoreCase(city)
				.map(LocationCountryEntity::getVoyageCode);

			if (voyageCodeByCity.isPresent()) {
				return voyageCodeByCity;
			}
		}

		// 2. 국가명으로 조회 (findFirstBy 적용: 결과가 7개여도 에러 안 남)
		if (!country.isEmpty()) {
			return locationCountryJpaRepository.findFirstByCountryName(country)
				.map(LocationCountryEntity::getVoyageCode);
		}

		return Optional.empty();
	}

}