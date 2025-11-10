package com.nexsol.cargo.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeyInPayment {

	private final Long userId;

	private final Long subscriptionId;

	private final String cardNo;

	private final String cardExpireYYMM;

	private final String cardPwd;

	private final String buyerAuthNum;

}
