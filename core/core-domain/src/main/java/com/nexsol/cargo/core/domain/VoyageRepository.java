package com.nexsol.cargo.core.domain;

import java.util.Optional;

public interface VoyageRepository {

	Optional<String> findVoyageCodeByOrigin(String origin);

}
