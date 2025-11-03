package com.nexsol.cargo.core.api.controller.v1.response;


import com.nexsol.cargo.core.domain.User;
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


    public static SignInResponse fromDomain(String accessToken, User user) {

        return SignInResponse.builder()
                .accessToken(accessToken)
                .companyCode(user.getCompanyCode())
                .companyName(user.getProfile().getCompanyName())
                .userName(user.getProfile().getUserName())
                .managerName(user.getProfile().getManagerName())
                .phoneNumber(user.getProfile().getPhoneNumber())
                .email(user.getProfile().getEmail())
                .build();

    }
}
