package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "special_policy_rate")
@NoArgsConstructor
public class SpecialPolicyRateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "item_middle_code", length = 20, nullable = false)
	private String itemMiddleCode;

	@Column(name = "item_detail_code", length = 20)
	private String itemDetailCode;

	@Column(name = "base_coverage_code", length = 50, nullable = false)
	private String baseCoverageCode;

	@Column(name = "voyage_code", length = 10)
	private String voyageCode;

	@Column(name = "packing_code", length = 10)
	private String packingCode;

	// ( ... 기타 모든 조건 컬럼 ... )

	@Column(name = "rate", precision = 10, scale = 5, nullable = false)
	private BigDecimal rate;

}
