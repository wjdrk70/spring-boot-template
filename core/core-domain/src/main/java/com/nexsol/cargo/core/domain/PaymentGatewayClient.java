package com.nexsol.cargo.core.domain;

import java.math.BigDecimal;

public interface PaymentGatewayClient {
    PgApprovalResult approve(String tid, String authToken, BigDecimal amount, String mid);

    // '승인' 실패 시 '망취소' 요청 (서버 to 서버)
    void netCancel(String tid, String authToken, BigDecimal amount, String mid);
}
