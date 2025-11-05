package com.nexsol.cargo.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfile {

	private Long userId;

	private String userName;

	private String companyName;

	private String managerName;

	private String phoneNumber;

	private String email;

	private Address address;

}
