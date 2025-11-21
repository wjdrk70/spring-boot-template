package com.nexsol.cargo.core.domain;

import java.util.Optional;

public interface LocationCountryRepository {

	Optional<LocationCountry> findByLocationCode(String locationCode);

	Optional<LocationCountry> findByCityNameEn(String cityNameEn);

}
