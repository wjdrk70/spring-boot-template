package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "tlo_rate",
		uniqueConstraints = {
				@UniqueConstraint(name = "uq_tlo_rate", columnNames = { "vessel_type", "bl_exists", "region" }) })
@NoArgsConstructor
public class TloRateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "vessel_type", length = 50, nullable = false)
	private String vesselType;

	@Column(name = "bl_exists", nullable = false)
	private Boolean blExists;

	@Column(name = "region", length = 100, nullable = false)
	private String region;

	@Column(name = "rate_percent", precision = 10, scale = 5, nullable = false)
	private BigDecimal ratePercent;

}