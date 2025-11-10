package com.nexsol.cargo.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TidMediumType {

	GENERAL("01"), // 일반결제
	PARTIAL_CANCEL("10"), // 부분취소
	BILLING("16"); // 빌링결제

	private final String code;

}
