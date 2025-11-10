package com.nexsol.cargo.core.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CoreErrorType {

	NOT_FOUND_DATA(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C1000, "해당 데이터를 찾지 못했습니다.", CoreErrorLevel.INFO),
	// Auth User
	USER_NOT_FOUND(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C1001, "해당 유저를 찾을 수 없습니다..", CoreErrorLevel.INFO),

	USER_EXIST_DATA(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.C1003, "해당 유저가 존재합니다.", CoreErrorLevel.INFO),
	AUTH_UNAUTHORIZED(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.C1002, "비밀번호가 틀렸습니다.", CoreErrorLevel.ERROR),

	// Plan
	CARGO_ITEM_NOT_FOUND(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C2000, "해당 HS Code에 맞는 화물 정보를 찾을 수 없습니다.",
			CoreErrorLevel.INFO),

	// pay
	PAYMENT_AUTH_FAILED(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.C4000, "PG사 인증에 실패했습니다.", CoreErrorLevel.WARN),
	PAYMENT_SIGNATURE_MISMATCH(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C4001, "결제 시그니처 검증에 실패했습니다.",
			CoreErrorLevel.ERROR),
	PAYMENT_AMOUNT_MISMATCH(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C4002, "결제 요청 금액과 DB 저장 금액이 일치하지 않습니다.",
			CoreErrorLevel.ERROR),
	PAYMENT_NOT_FOUND(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.C4003, "결제 정보를 찾을 수 없습니다.", CoreErrorLevel.WARN);

	private final CoreErrorKind kind;

	private final CoreErrorCode code;

	private final String message;

	private final CoreErrorLevel level;

}
