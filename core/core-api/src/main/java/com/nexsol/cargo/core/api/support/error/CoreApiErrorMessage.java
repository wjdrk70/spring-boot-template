package com.nexsol.cargo.core.api.support.error;

/**
 *
 * 불변성, equals, hashCode, toString, 접근자를 자동으로 생성
 */
public record CoreApiErrorMessage(String code, String message, Object data) {

	/**
	 * @param coreApiErrorType 에러 타입
	 * @param data 관련 데이터
	 */
	public CoreApiErrorMessage(CoreApiErrorType coreApiErrorType, Object data) {
		this(coreApiErrorType.getCode().name(), coreApiErrorType.getMessage(), data);
	}

	/**
	 * data 필드가 없는 경우를 위한 오버로딩 생성자
	 * @param coreApiErrorType 에러 타입
	 */
	public CoreApiErrorMessage(CoreApiErrorType coreApiErrorType) {
		this(coreApiErrorType, null);
	}
}