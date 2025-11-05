package com.nexsol.cargo.core.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PremiumCalculator {
    private static final String KRW_CURRENCY = "KRW";


    public BigDecimal calculate(
            BigDecimal invoiceAmount,
            BaseCoverage baseCoverage,
            List<OptionCoverage> options,
            String currencyUnit,
            BigDecimal exchangeRateAmount) {


        // 1. 기본 요율 조회
        BigDecimal baseRate = getBaseRate(baseCoverage.getCode());

        // 2. 옵션 요율 합산
        BigDecimal optionsRate = options.stream()
                .map(option -> getOptionRate(option.getCode()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. 통상 요율 계산
        BigDecimal totalRate = baseRate.add(optionsRate);

        // 4. 보험료 산출
        BigDecimal calculatedPremium = invoiceAmount.multiply(totalRate);

        //5. 환율 적용
        BigDecimal premium=calculatedPremium;

        if (!currencyUnit.toUpperCase().equals(KRW_CURRENCY)) {

            // 보험료(원화) = 보험료(외화) x 환율 금액
            premium = calculatedPremium.multiply(exchangeRateAmount)
                    .setScale(0, RoundingMode.HALF_UP);
        } else {
            // KRW인 경우, 보험료는 소수점 2자리까지 유지
            premium = premium.setScale(2, RoundingMode.HALF_UP);
        }

        return premium;
    }

    private BigDecimal getBaseRate(String code) {
        return new BigDecimal("0.015");
    }

    private BigDecimal getOptionRate(String code) {
        return BigDecimal.ZERO;
    }
}