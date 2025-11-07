package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConveyanceType;
import com.nexsol.cargo.core.enums.RateType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PremiumCalculator {

	private static final String KRW_CURRENCY = "KRW";

	public BigDecimal calculate(CargoDetail cargoDetail, BigDecimal exchangeRateAmount, BaseCoverage baseCoverage,
			List<OptionCoverage> options) {

		// 1. 기본 요율 조회
		BigDecimal baseRate = baseCoverage.getRate(); // e.g., 0.015

		// 2. 부가위험요율 합
		BigDecimal sumOfAdditionalRiskRates = options.stream()
			.filter(option -> option.getRateType() == RateType.ADDITIONAL_RISK)
			.map(OptionCoverage::getRate)
			.reduce(BigDecimal.ZERO, BigDecimal::add); // e.g., 0.002 + 0.001

		// 3. 확장담보요율
		BigDecimal extensionRate = options.stream()
			.filter(o -> o.getRateType() == RateType.EXTENSION)
			.map(OptionCoverage::getRate)
			.reduce(BigDecimal.ZERO, BigDecimal::add); // e.g., 0.0

		// 4. 특약요율 합(Σ)
		BigDecimal sumOfSpecialClauseRates = options.stream()
			.filter(o -> o.getRateType() == RateType.SPECIAL_CLAUSE)
			.map(OptionCoverage::getRate)
			.reduce(BigDecimal.ZERO, BigDecimal::add); // e.g., 0.005

		// 5. 추가 전손요율
		BigDecimal totalLossRate = calculateTotalLossRate(cargoDetail.conveyance(), cargoDetail.origin(),
				cargoDetail.destination());

		// 6. 추가할증요율 계산 (e.g., 선박할증)
		BigDecimal surchargeRate = BigDecimal.ZERO;

		// 통상 요율
		BigDecimal totalRate = baseRate.add(sumOfAdditionalRiskRates)
			.add(extensionRate)
			.add(sumOfSpecialClauseRates)
			.add(totalLossRate)
			.add(surchargeRate);

		// 보험료 산출
		BigDecimal premium = getPremium(cargoDetail, exchangeRateAmount, totalRate);

		return premium;
	}

	private static BigDecimal getPremium(CargoDetail cargoDetail, BigDecimal exchangeRateAmount, BigDecimal totalRate) {
		BigDecimal calculatedPremium = cargoDetail.invoiceAmount().multiply(totalRate);

		// 환율 적용
		BigDecimal premium = calculatedPremium;

		if (!cargoDetail.currencyUnit().toUpperCase().equals(KRW_CURRENCY)) {

			// 보험료(원화) = 보험료(외화) x 환율 금액
			premium = calculatedPremium.multiply(exchangeRateAmount).setScale(0, RoundingMode.HALF_UP);
		}
		else {
			// KRW인 경우, 보험료는 소수점 2자리까지 유지
			premium = premium.setScale(2, RoundingMode.HALF_UP);
		}
		return premium;
	}

	private BigDecimal calculateTotalLossRate(ConveyanceType conveyance, String origin, String destination) {
		// TODO:의 로직 구현 추후 수정
		// e.g., if (conveyance == ConveyanceType.SHIP && "철선") { return 0.001; (70% 기준) }
		return BigDecimal.ZERO; // TODO: 임시 추후 수정
	}

}