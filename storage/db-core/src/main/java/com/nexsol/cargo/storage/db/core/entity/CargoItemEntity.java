package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cargo_item")
@Getter
@NoArgsConstructor
public class CargoItemEntity extends BaseEntity {

	@Column(name = "name", nullable = false, unique = true) // V1_init_tables의 'code' 대신
															// 'name'을 사용 (사용자 요청 반영)
	private String name;

	@Column(name = "hs_code", nullable = false, unique = true)
	private String hsCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rider_id", nullable = false)
	private CoverageBaseEntity rider;

}