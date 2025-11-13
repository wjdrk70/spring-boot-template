package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voyage")
@Getter
@NoArgsConstructor
public class VoyageEntity {

	@Id
	@Column(name = "voyage_code", length = 10, nullable = false)
	private String voyageCode;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

}
