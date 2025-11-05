package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.CoverageOptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OptionCoverage {

	private final String code;

	private final String name;

	private final CoverageOptionType type;

}
