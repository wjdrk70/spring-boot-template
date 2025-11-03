package com.nexsol.cargo.core.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PremiumCalculator {

    public BigDecimal calculate(
            BigDecimal invoiceAmount,
            BaseCoverage baseCoverage,
            List<OptionCoverage> options) {

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

        // (기타 할인, 할증, 최저보험료 로직...)

        return new BigDecimal("21090.00"); // 임시 반환
    }

    private BigDecimal getBaseRate(String code) {
        return new BigDecimal("0.015");
    }

    private BigDecimal getOptionRate(String code) {
        return BigDecimal.ZERO;
    }
}