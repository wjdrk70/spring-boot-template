package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.enums.SpecialRateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "special_risk_rate",
		uniqueConstraints = { @UniqueConstraint(name = "uq_special_risk_rate",
				columnNames = { "risk_type", "clause_name", "condition_name" }) })
@NoArgsConstructor
public class SpecialRiskRateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "risk_type", length = 100, nullable = false)
	private String riskType;

	@Column(name = "clause_name", length = 100, nullable = false)
	private String clauseName;

	@Column(name = "condition_name", length = 100)
	private String conditionName;

	@Column(name = "rate", precision = 10, scale = 5, nullable = false)
	private BigDecimal rate;

	@Enumerated(EnumType.STRING)
	@Column(name = "rate_type", nullable = false)
	private SpecialRateType rateType;

}
