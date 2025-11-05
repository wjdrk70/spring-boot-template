package com.nexsol.cargo.core.domain;

import java.math.BigDecimal;

public record PaymentReadyResult(
        String tid, // PG사에 전달할 거래 ID
        BigDecimal amount, // 결제 금액
        String signData // PG SDK 연동용 서명 값
        // TODO:(기타 SDK 연동에 필요한 값들)
) {
}