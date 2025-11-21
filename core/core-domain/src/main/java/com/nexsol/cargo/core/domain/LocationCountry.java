package com.nexsol.cargo.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationCountry {

	private Long id;

	private String locationCode;

	private String countryNameEn;

	private String cityNameEn;

	private String voyageCode;

}