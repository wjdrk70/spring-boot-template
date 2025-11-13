package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PremiumCalculator {

	private static final BigDecimal PROFIT_MARGIN = new BigDecimal("1.10"); // 희망 인상률 110%
																			// 고정

	private final BaseRateFinder baseRateFinder;

	private final CargoItemFinder cargoItemFinder;

	private final VoyageFinder voyageFinder;

	private final PremiumAdjust premiumAdjust;

	// TODO: 계산 로직 확립 이후 주석 모두 제거
	public BigDecimal calculate(CargoDetail cargoDetail, BigDecimal exchangeRateAmount, BaseCoverage baseCoverage,
			List<OptionCoverage> options) {
		// 기본요율 조회
		String cargoItemCode = cargoItemFinder.find(cargoDetail.hsCode());
		String voyageCode = voyageFinder.find(cargoDetail.origin());
		BigDecimal baseRate = baseRateFinder.find(cargoItemCode, baseCoverage.getCode(), voyageCode);

		// TODO: 현재는 요율 0으로 임의지정 코드 데이터 들어오면 비즈니스 로직 전환
		// 통상요율 계산 (TODO 반영)
		BigDecimal normalRate = baseRate.add(BigDecimal.ZERO); // (부가위험 등 합산)

		// 적용특칙요율 계산 (TODO 반영)
		BigDecimal specialPolicyRate = BigDecimal.ZERO;

		// 최종요율 계산 (TODO 반영)
		BigDecimal finalRate = normalRate.subtract(specialPolicyRate);

		// 보험가입금액 계산
		BigDecimal insuredAmount = cargoDetail.invoiceAmount().multiply(PROFIT_MARGIN);

		// 보험료 산출
		BigDecimal calculatedPremium = finalRate.multiply(insuredAmount);

		// 최종 보험료 조정
		return premiumAdjust.adjust(calculatedPremium, cargoDetail.currencyUnit(), exchangeRateAmount);
	}

}