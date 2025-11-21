package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "location_country")
@NoArgsConstructor
public class LocationCountryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String locationCode;

	@Column(nullable = false)
	private String countryName;

	@Column
	private String cityName;

	@Column(nullable = false)
	private String voyageCode;

}
