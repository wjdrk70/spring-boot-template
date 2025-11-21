package com.nexsol.cargo.core.api.controller.v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveSignatureRequest {

	@NotNull(message = "청약 ID는 필수 입니다.")
	private Long subscriptionId;

	@NotBlank(message = "전자 서명 데이터는 필수입니다.")
	private String signatureBase64;

	@NotBlank(message = "이미지 Content-Type은 필수입니다.")
	private String contentType;

}
