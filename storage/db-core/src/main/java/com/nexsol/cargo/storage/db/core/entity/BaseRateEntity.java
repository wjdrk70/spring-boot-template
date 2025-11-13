package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "base_rate",
		uniqueConstraints = { @UniqueConstraint(name = "uq_base_rate",
				columnNames = { "middle_code", "base_coverage_code", "voyage_code" }) })
@NoArgsConstructor
public class BaseRateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "middle_code", length = 20, nullable = false)
	private String middleCode;

	@Column(name = "base_coverage_code", length = 50, nullable = false)
	private String baseCoverageCode;

	@Column(name = "voyage_code", length = 10, nullable = false)
	private String voyageCode;

	@Column(name = "rate", precision = 10, scale = 5, nullable = false)
	private BigDecimal rate;

}
