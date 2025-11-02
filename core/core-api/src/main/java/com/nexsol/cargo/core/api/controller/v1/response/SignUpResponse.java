package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignUpResponse {

	private Long userId;

	private String companyCode;

	private String name;

	private String companyName;

	private String email;

	private UserRole role;

	public static SignUpResponse fromDomain(User user) {
		return SignUpResponse.builder()
			.userId(user.getId())
			.companyCode(user.getCompanyCode())
			.companyName(user.getProfile().getCompanyName())
			.email(user.getProfile().getEmail())
			.name(user.getName())
			.role(user.getRole())
			.build();
	}

}
