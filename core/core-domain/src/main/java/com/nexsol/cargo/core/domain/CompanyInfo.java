package com.nexsol.cargo.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInfo {

	private Long id;

	private String companyName;

	private String companyCode;

	private String managerName;

	private String phoneNumber;

	private String email;

	private Address address;

}
