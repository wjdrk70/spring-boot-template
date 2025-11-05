package com.nexsol.cargo.core.domain;

import java.math.BigDecimal;
import java.util.Set;

public record CreateSubscription(Long userId, String hsCode, BigDecimal invoiceAmount, String currencyUnit,
		BigDecimal exchangeRateAmount, Set<String> coverageCodes, boolean isSame, String policyholderCompanyName,
		String policyholderCompanyCode, String insuredCompanyName, String insuredCompanyCode, CargoDetail cargoDetail

) {
}
