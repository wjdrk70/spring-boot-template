package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.CoverageOptionType;
import com.nexsol.cargo.core.enums.RateType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@Builder
public class OptionCoverage {

	private final String code;

	private final String name;

	private final CoverageOptionType optionType;

	private final RateType rateType;

	private final BigDecimal rate;

}
