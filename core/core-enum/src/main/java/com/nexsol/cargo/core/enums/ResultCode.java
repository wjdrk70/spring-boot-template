package com.nexsol.cargo.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

	SUCCESS_CREDIT_CARD("3001", "신용카드 성공코드"), SUCCESS_ACCOUNT_TRANSFER("4000", "계좌이체 성공코드"),
	SUCCESS_VIRTUAL_ACCOUNT("4100", "가상계좌 발급 성공코드"), SUCCESS_MOBILE_PAYMENT("A000", "휴대폰 소액결제 성공코드"),
	CASH_RECEIPT("7001", "현금영수증");

	private final String code;

	private final String description;

	public static ResultCode findByCode(String code) {
		if (code == null) {
			return null;
		}
		for (ResultCode resultCode : values()) {
			if (resultCode.code.equals(code)) {
				return resultCode;
			}
		}
		return null;
	}

}
