package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.domain.Address;
import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.domain.UserProfile;
import com.nexsol.cargo.core.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

	@NotBlank(message = "비밀번호는 필수입니다")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
			message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다")
	private String password;

	// UserProfile
	@NotBlank(message = "사업자명은 필수입니다")
	private String userName;

	@NotBlank(message = "상호명은 필수입니다")
	private String companyName;

	@NotBlank(message = "사업자번호는 필수입니다")
	private String companyCode;

	@NotBlank(message = "담당자명은 필수입니다")
	private String managerName;

	@NotBlank(message = "휴대폰번호는 필수입니다")
	@Pattern(regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다")
	private String phoneNumber;

	@NotBlank(message = "이메일은 필수입니다")
	@Email(message = "올바른 이메일 형식이 아닙니다")
	private String email;

	// Address
	@NotBlank(message = "우편번호는 필수입니다")
	private String zipCode;

	@NotBlank(message = "주소는 필수입니다")
	private String addressLine;

	private String addressDetail;

	public User toUserDomain() {
		return User.builder().companyCode(this.companyCode).password(this.password).role(UserRole.ADMIN).build();
	}

	public UserProfile toProfileDomain() {
		return UserProfile.builder()
			.userName(this.userName)
			.companyName(this.companyName)
			.managerName(this.managerName)
			.phoneNumber(this.phoneNumber)
			.email(this.email)
			.address(Address.builder()
				.zipCode(this.zipCode)
				.addressLine(this.addressLine)
				.addressDetail(this.addressDetail)
				.build())
			.build();
	}

}