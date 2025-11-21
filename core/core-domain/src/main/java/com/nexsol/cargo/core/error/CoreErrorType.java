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
	CARGO_ITEM_NOT_FOUND(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C2000, "해당 적하품목이 없습니다.", CoreErrorLevel.INFO),
	VOYAGE_NOT_FOUND(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C2001, "출발지에 해당하는 항해구간코드를 찾을 수 없습니다.",
			CoreErrorLevel.INFO),
	BASE_RATE_NOT_FOUND(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C2002, "기본 요율을 찾을 수 없습니다.", CoreErrorLevel.INFO),

	// pay
	PAYMENT_AUTH_FAILED(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.C4000, "PG사 인증에 실패했습니다.", CoreErrorLevel.WARN),
	PAYMENT_SIGNATURE_MISMATCH(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C4001, "결제 시그니처 검증에 실패했습니다.",
			CoreErrorLevel.ERROR),
	PAYMENT_AMOUNT_MISMATCH(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C4002, "결제 요청 금액과 DB 저장 금액이 일치하지 않습니다.",
			CoreErrorLevel.ERROR),
	PAYMENT_ALREADY_PAID(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C4005, "이미 결제된 건입니다.", CoreErrorLevel.WARN),
	PAYMENT_NOT_FOUND(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.C4003, "결제 정보를 찾을 수 없습니다.", CoreErrorLevel.WARN),
	PAYMENT_CANCEL_FAILED(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C4004, "이미 취소되었거나 성공한 결제가 아닙니다.",
			CoreErrorLevel.INFO),

	// subscription
	POLICY_CANNOT_BE_ISSUED(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.C5000, "결제가 완료되지 않아 증권을 발급할 수 없습니다.",
			CoreErrorLevel.INFO),
	SUBSCRIPTION_NOT_FOUND_SIGNATURE(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C5001, "전자 서명 이미지가 존재하지 않습니다.",
			CoreErrorLevel.INFO),
	SUBSCRIPTION_SIGNATURE_UPLOAD_FAILED(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C5002, "전자 서명 업로드 실패",
			CoreErrorLevel.INFO),
	SUBSCRIPTION_SIGNATURE_DOWNLOAD_FAILED(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C5003, "전자 서명 다운로드 실패",
			CoreErrorLevel.INFO),
	SUBSCRIPTION_PRESIGNED_URL_FAILED(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C5004, "Presigned URL 생성 실패",
			CoreErrorLevel.INFO),
	SUBSCRIPTION_PENDING_FAILED(CoreErrorKind.SERVER_ERROR, CoreErrorCode.C5005, "결제가 완료되었거나,취소된 청약입니다.",
			CoreErrorLevel.INFO);

	private final CoreErrorKind kind;

	private final CoreErrorCode code;

	private final String message;

	private final CoreErrorLevel level;

}
