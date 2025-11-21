package com.nexsol.cargo.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

	CARD_KEY_IN("신용카드"), SIMPLE_PAY("간편결제"), BANK_TRANSFER("실시간 계좌이체"), VIRTUAL_ACCOUNT("가상계좌");

	private final String title;

}
