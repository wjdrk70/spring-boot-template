package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.enums.SurchargeRateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "surcharge_rate",
		uniqueConstraints = {
				@UniqueConstraint(name = "uq_surcharge_rate", columnNames = { "surcharge_type", "condition_name" }) })
@NoArgsConstructor
public class SuchargeRateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "surcharge_type", length = 100, nullable = false)
	private String surchargeType;

	@Column(name = "condition_name", length = 100, nullable = false)
	private String conditionName;

	@Column(name = "rate", precision = 10, scale = 5, nullable = false)
	private BigDecimal rate;

	@Enumerated(EnumType.STRING)
	@Column(name = "rate_type", nullable = false)
	private SurchargeRateType rateType;

}
