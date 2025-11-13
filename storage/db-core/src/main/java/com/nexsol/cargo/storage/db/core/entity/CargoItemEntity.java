package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cargo_item", indexes = { @Index(name = "idx_middle_code", columnList = "middle_code") })
@Getter
@NoArgsConstructor
public class CargoItemEntity {

	@Id
	@Column(name = "hs_code", length = 20, nullable = false)
	private String hsCode;

	@Column(name = "middle_code", length = 20, nullable = false)
	private String middleCode;

	@Column(name = "name", length = 255)
	private String name;

}