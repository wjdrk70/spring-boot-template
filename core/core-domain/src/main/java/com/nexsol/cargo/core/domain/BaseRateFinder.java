package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BaseRateFinder {

	private final BaseRateRepository baseRateRepository;

	public BigDecimal find(String middleCode, String baseCoverageCode, String voyageCode) {
		try {

			return baseRateRepository.findRate(middleCode, baseCoverageCode, voyageCode)
				.orElseThrow(() -> new CoreException(CoreErrorType.BASE_RATE_NOT_FOUND));

		}
		catch (Exception e) {
			// TODO: 요율 조회 실패시 logging
			return BigDecimal.ZERO;
		}
	}

}
