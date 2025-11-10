package com.nexsol.cargo.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TidPaymentMethod {

	CARD("01"), BANK("02"), VBANK("03"), CELLPHONE("05");

	private final String code;

}
