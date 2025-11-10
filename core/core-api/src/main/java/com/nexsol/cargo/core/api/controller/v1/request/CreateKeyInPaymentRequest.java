package com.nexsol.cargo.core.api.controller.v1.request;

import com.nexsol.cargo.core.domain.KeyInPayment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateKeyInPaymentRequest {

	@NotNull(message = "청약 ID는 필수입니다.")
	private Long subscriptionId;

	@NotBlank(message = "카드번호는 필수입니다.")
	@Size(min = 16, max = 16, message = "카드번호 16자리를 입력하세요.")
	private String cardNo;

	@NotBlank(message = "유효기간(연도)은 필수입니다.")
	@Size(min = 2, max = 2, message = "유효기간(YY) 2자리를 입력하세요.")
	private String cardExpireYY;

	@NotBlank(message = "유효기간(월)은 필수입니다.")
	@Size(min = 2, max = 2, message = "유효기간(MM) 2자리를 입력하세요.")
	private String cardExpireMM;

	@NotBlank(message = "카드 비밀번호 앞 2자리는 필수입니다.")
	@Size(min = 2, max = 2, message = "비밀번호 앞 2자리를 입력하세요.")
	private String cardPwd; // 카드 비밀번호 앞 2자리

	@NotBlank(message = "인증번호는 필수입니다.")
	@Size(min = 6, message = "생년월일(YYMMDD) 6자리 또는 사업자번호 10자리를 입력하세요.")
	private String buyerAuthNum; // 생년월일(YYMMDD) 6자리 또는 사업자번호 10자리

	public KeyInPayment toKeyInPayment(Long userId) {
		return KeyInPayment.builder()
			.userId(userId)
			.subscriptionId(this.subscriptionId)
			.cardNo(this.cardNo)
			.cardExpireYYMM(this.cardExpireYY + this.cardExpireMM) // YYMM 형식으로 조합
			.cardPwd(this.cardPwd)
			.buyerAuthNum(this.buyerAuthNum)
			.build();
	}

}
