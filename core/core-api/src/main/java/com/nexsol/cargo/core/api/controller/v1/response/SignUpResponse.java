package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SignUpResponse {

	private Long userId;

	private String loginId;

	private String name;

	private String companyName;

	private String email;

	private UserRole role;

	public static SignUpResponse fromDomain(User user) {
		return SignUpResponse.builder()
			.userId(user.getId())
			.loginId(user.getLoginId())
			.companyName(user.getCompanyInfo().getCompanyName())
			.email(user.getCompanyInfo().getEmail())
			.name(user.getName())
			.role(user.getRole())
			.build();
	}

}
