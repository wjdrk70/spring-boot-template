package com.nexsol.cargo.client.nts.client;

import com.nexsol.cargo.client.nts.client.support.NicePayUtil;
import com.nexsol.cargo.core.domain.PaymentGatewayClient;
import com.nexsol.cargo.core.domain.PgApprovalResult;
import com.nexsol.cargo.core.domain.PgCancelResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NicePaymentsClient implements PaymentGatewayClient {

	@Value("${nice-pay.mid}")
	private String mid;

	@Value("${nice-pay.merchant-key}")
	private String merchantKey;

	@Value("${nice-pay.url.key-in}")
	private String keyInUrl;

	@Value("${nice-pay.url.cancel}")
	private String cancelUrl;

	private static final String RESULT_CODE_SUCCESS = "3001";

	private static final String RESULT_CODE_CANCEL_SUCCESS = "2001";

	private final NicePayUtil nicePayUtil;

	@Override
	public PgApprovalResult approve(String txTid, String authToken, BigDecimal amount, String mid, String NextAppURL) {
		String ediDate = nicePayUtil.getEdiDate();
		String amtString = amount.toPlainString();

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("TID", txTid);
		formData.add("AuthToken", authToken);
		formData.add("MID", mid);
		formData.add("Amt", amtString);
		formData.add("EdiDate", ediDate);
		formData.add("SignData", nicePayUtil.makeSignature(authToken + mid + amtString + ediDate + merchantKey));
		formData.add("EdiType", "JSON");
		formData.add("CharSet", "euc-kr");

		log.info("[NicePayments] 승인 요청. TID: {}", txTid);

		Map<String, String> responseMap = nicePayUtil.sendRequest(NextAppURL, formData);

		nicePayUtil.validateResponse(responseMap, txTid);

		return new PgApprovalResult(responseMap.get("AuthCode"), responseMap.get("CardCode"));

	}

	@Override
	public void netCancel(String txTid, String authToken, BigDecimal amount, String mid, String NetCancelURL) {
		String ediDate = nicePayUtil.getEdiDate();
		String amtString = amount.toPlainString();

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("TID", txTid);
		formData.add("AuthToken", authToken);
		formData.add("MID", mid);
		formData.add("Amt", amtString);
		formData.add("EdiDate", ediDate);
		formData.add("SignData", nicePayUtil.makeSignature(authToken + mid + amtString + ediDate + merchantKey));
		formData.add("NetCancel", "1");
		formData.add("EdiType", "JSON");
		formData.add("CharSet", "euc-kr");

		log.warn("[NicePayments] 망취소 요청. TID: {}", txTid);

		try {
			// 망취소는 응답 검증보다는 호출 자체에 의의를 둠
			nicePayUtil.sendRequest(NetCancelURL, formData);
		}
		catch (Exception e) {
			log.error("[NicePayments] 망취소 실패. TID: {}", txTid, e);
			// 💡 해결: 망취소 실패도 CoreException 등으로 감싸서 다시 던집니다.
			throw new RuntimeException("PG 망취소 요청 실패", e);
		}

	}

	@Override
	public PgApprovalResult keyInPayment(String tid, String moid, String amt, String encData, String signData,
			String ediDate, String goodsName) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("TID", tid);
		formData.add("MID", this.mid);
		formData.add("EdiDate", ediDate);
		formData.add("Moid", moid);
		formData.add("Amt", amt);
		formData.add("EncData", encData);
		formData.add("SignData", signData);
		formData.add("GoodsName", goodsName);
		formData.add("CardInterest", "0");
		formData.add("CardQuota", "00");
		formData.add("EdiType", "JSON");
		formData.add("CharSet", "euc-kr");

		log.info("[NicePayments] Key-In 요청. TID: {}", tid);

		Map<String, String> responseMap = nicePayUtil.sendRequest(this.keyInUrl, formData);

		if (!RESULT_CODE_SUCCESS.equals(responseMap.get("ResultCode"))) {
			throw new RuntimeException("카드 결제 실패: " + responseMap.get("ResultMsg"));
		}

		return new PgApprovalResult(responseMap.get("AuthCode"), responseMap.get("CardCode"));
	}

	@Override
	public PgCancelResult cancel(String tid, String mid, String moid, String cancelAmt, String reason, String ediDate,
			String signData) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("TID", tid);
		formData.add("MID", mid);
		formData.add("Moid", moid);
		formData.add("CancelAmt", cancelAmt);
		formData.add("CancelMsg", reason);
		formData.add("PartialCancelCode", "0");
		formData.add("EdiDate", ediDate);
		formData.add("SignData", signData);
		formData.add("EdiType", "JSON");
		formData.add("CharSet", "euc-kr");

		log.info("[NicePayments] 취소 요청. TID: {}", tid);

		Map<String, String> responseMap = nicePayUtil.sendRequest(this.cancelUrl, formData);

		if (!RESULT_CODE_CANCEL_SUCCESS.equals(responseMap.get("ResultCode"))) {
			throw new RuntimeException("PG 취소 실패: " + responseMap.get("ResultMsg"));
		}

		return new PgCancelResult(responseMap.get("ResultCode"), responseMap.get("ResultMsg"),
				responseMap.get("CancelAmt"), responseMap.get("CancelDate"), responseMap.get("CancelTime"));
	}

}