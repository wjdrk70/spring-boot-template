package com.nexsol.cargo.core.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PremiumAdjust {

	private static final String KRW_CURRENCY = "KRW";

	private static final BigDecimal MINIMUM_PREMIUM_USD = new BigDecimal("15"); // 최저 보험료
																				// USD 15

	public BigDecimal adjust(BigDecimal calculatedPremium, String currencyUnit, BigDecimal exchangeRateAmount) {

		BigDecimal premiumInKrw;

		// 1. 환율 적용 (원화로 변환)
		if (!KRW_CURRENCY.equalsIgnoreCase(currencyUnit)) {
			premiumInKrw = calculatedPremium.multiply(exchangeRateAmount).setScale(0, RoundingMode.HALF_UP);
		}
		else {
			premiumInKrw = calculatedPremium.setScale(0, RoundingMode.HALF_UP);
		}

		// 2. 최저 보험료 적용
		BigDecimal minPremiumInKrw = MINIMUM_PREMIUM_USD.multiply(exchangeRateAmount).setScale(0, RoundingMode.HALF_UP);

		if (premiumInKrw.compareTo(minPremiumInKrw) < 0) {
			return minPremiumInKrw;
		}

		return premiumInKrw;
	}

}
