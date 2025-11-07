package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.OptionCoverage;
import com.nexsol.cargo.core.enums.CoverageOptionType;
import com.nexsol.cargo.core.enums.RateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "coverage_option")
@Getter
@NoArgsConstructor
public class CoverageOptionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "rate", nullable = false)
	private BigDecimal rate;

	@Enumerated(EnumType.STRING)
	@Column(name = "option_type", nullable = false)
	private CoverageOptionType optionType;

	@Enumerated(EnumType.STRING)
	@Column(name = "rate_type", nullable = false)
	private RateType rateType;

	public OptionCoverage toDomain() {
		return OptionCoverage.builder()
			.code(this.code)
			.name(this.name)
			.optionType(this.optionType)
			.rateType(this.rateType)
			.rate(this.rate)
			.build();
	}

}