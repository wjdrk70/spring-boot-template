package com.nexsol.cargo.core.domain;

import java.math.BigDecimal;

public interface PaymentGatewayClient {

	PgApprovalResult approve(String txTid, String authToken, BigDecimal amt, String mid, String NextAppURL);

	// '승인' 실패 시 '망취소' 요청 (서버 to 서버)
	void netCancel(String txTid, String authToken, BigDecimal amt, String mid, String NetCancelURL);

	PgApprovalResult keyInPayment(String tid, String moid, String amt, String encData, String signData, String ediDate,
			String goodsName);

}
