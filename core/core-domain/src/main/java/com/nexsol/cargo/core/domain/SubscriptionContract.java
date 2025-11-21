package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionContract(Long subscriptionId, // 순번 처리용
		String policyNumber, // 증권번호
		SubscriptionStatus status, // 상태 (신규/해지 등)
		String policyholderName, // 계약자명
		String businessNumber, // 사업자번호
		String managerName, // 담당자명 (from UserProfile)
		String managerPhone, // 휴대폰번호 (from UserProfile)
		LocalDate contractDate, // 계약일자
		LocalDate startDate, // 보험기간 시작
		LocalDate endDate, // 보험기간 종료
		BigDecimal premium, // 보험료
		String paymentMethod) {
}