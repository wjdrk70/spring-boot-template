package com.nexsol.cargo.client.nts.client;

import com.nexsol.cargo.core.domain.PaymentGatewayClient;
import com.nexsol.cargo.core.domain.PgApprovalResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NicePaymentsClient implements PaymentGatewayClient {

	// (TODO:PG연동시 RestTemplate, WebClient 등 HTTP 클라이언트 주입) ? FeignClient 로 할까? 뭐로하지...

	@Override
	public PgApprovalResult approve(String tid, String authToken, BigDecimal amount, String mid) {
		// TODO: Nice Payments '승인' API 실제 호출 참조 불가의 벽
		return new PgApprovalResult("pgAuthCode123", "01");
	}

	@Override
	public void netCancel(String tid, String authToken, BigDecimal amount, String mid) {
		// TODO: Nice Payments '망취소' API 실제 호출 참조 불가의 벽

	}

}
