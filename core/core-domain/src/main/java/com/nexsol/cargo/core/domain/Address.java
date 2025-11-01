package com.nexsol.cargo.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

	private String zipCode;

	private String addressLine;

	private String addressDetail;

}