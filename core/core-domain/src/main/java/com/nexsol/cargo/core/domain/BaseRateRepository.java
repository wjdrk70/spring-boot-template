package com.nexsol.cargo.core.domain;

import java.math.BigDecimal;
import java.util.Optional;

public interface BaseRateRepository {

	Optional<BigDecimal> findRate(String middleCode, String baseCoverageCode, String voyageCode);

}
