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

	private String loginId;

	private String password;

	private String name;

	private UserProfile profile;

	private CompanyInfo companyInfo;

	private UserRole role;

	public String getName() {
		return profile != null ? profile.getName() : null;
	}

}
