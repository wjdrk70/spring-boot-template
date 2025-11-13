package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "extension_rate",
		uniqueConstraints = { @UniqueConstraint(name = "uq_extension_rate",
				columnNames = { "risk_name", "risk_condition", "period_days" }) })
@NoArgsConstructor
public class ExtensionRateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "risk_name", length = 100, nullable = false)
	private String riskName;

	@Column(name = "risk_condition", length = 100, nullable = false)
	private String riskCondition;

	@Column(name = "period_days", nullable = false)
	private Integer periodDays;

	@Column(name = "rate", precision = 10, scale = 5, nullable = false)
	private BigDecimal rate;

}