package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

	private Long id;

	private String companyCode;

	private String password;

	private UserProfile profile;

	private UserRole role;

	public String getUserName() {
		return profile != null ? profile.getUserName() : null;
	}

}
