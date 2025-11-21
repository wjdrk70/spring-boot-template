package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.BaseCoverage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "coverage_base")
@Getter
@NoArgsConstructor
public class CoverageBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	public BaseCoverage toDomain() {
		return BaseCoverage.builder().code(this.code).name(this.name).build();
	}

}