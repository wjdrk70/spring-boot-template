package com.nexsol.cargo.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@Builder
public class BaseCoverage {

	private final String code;

	private final String name;

	private final BigDecimal rate;

}
