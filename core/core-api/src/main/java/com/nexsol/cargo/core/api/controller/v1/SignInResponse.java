package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.domain.UserProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponse {

	private String accessToken;

	private String companyCode;

	private String companyName;

	private String userName; // 담당자 이름

	private String managerName; // 담당자 명

	private String phoneNumber;

	private String email;

	public static SignInResponse fromDomain(String accessToken, User user, UserProfile profile) {
		return SignInResponse.builder()
			.accessToken(accessToken)
			.companyCode(user.getCompanyCode())
			.companyName(profile.getCompanyName())
			.userName(profile.getUserName())
			.managerName(profile.getManagerName())
			.phoneNumber(profile.getPhoneNumber())
			.email(profile.getEmail())
			.build();

	}

}
