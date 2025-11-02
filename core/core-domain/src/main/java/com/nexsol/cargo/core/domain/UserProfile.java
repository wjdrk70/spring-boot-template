package com.nexsol.cargo.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfile {

	private String userName;

	private String companyName;

	private String companyCode;

	private String managerName;

	private String phoneNumber;

	private String email;

	private Address address;

}
