package com.nexsol.cargo.core.api.controller.v1.request;

import com.nexsol.cargo.core.domain.CreateSubscription;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class CreateSubscriptionRequest {

	@NotBlank(message = "가견적 Key는 필수입니다.")
	private String quotationKey;

	@NotEmpty(message = "선택한 담보 코드는 필수입니다.")
	private Set<String> coverageCodes;

	@NotNull(message = "계약자/피보험자 동일 여부는 필수입니다.")
	private Boolean isSame;

	@NotBlank(message = "계약자 상호명은 필수입니다.")
	private String policyholderCompanyName;

	@NotBlank(message = "계약자 사업자번호는 필수입니다.")
	private String policyholderCompanyCode;

	private String insuredCompanyName;

	private String insuredCompanyCode;

	@NotBlank(message = "담당자명은 필수입니다.")
	private String managerName;

	@NotBlank(message = "담당자 연락처는 필수입니다.")
	private String managerPhone;

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String managerEmail;

	public CreateSubscription toCreateSubscription(Long userId) {

		return CreateSubscription.builder()
			.policyholderCompanyCode(this.policyholderCompanyCode)
			.isSame(this.isSame)
			.quotationKey(this.quotationKey)
			.coverageCodes(this.coverageCodes)
			.userId(userId)
			.policyholderCompanyName(this.policyholderCompanyName)
			.insuredCompanyName(this.insuredCompanyName)
			.insuredCompanyCode(this.insuredCompanyCode)
			.managerName(this.managerName)
			.managerPhone(this.managerPhone)
			.managerEmail(this.managerEmail)
			.build();

	}

}
