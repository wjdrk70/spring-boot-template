package com.nexsol.cargo.core.domain;

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
				.orElseThrow(() -> new IllegalArgumentException("기본 요율을 찾을 수 없습니다."));

		}
		catch (Exception e) {
			// TODO: 요율 조회 실패시 logging
			return BigDecimal.ZERO;
		}
	}

}
