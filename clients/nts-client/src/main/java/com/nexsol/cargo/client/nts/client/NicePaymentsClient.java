package com.nexsol.cargo.client.nts.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexsol.cargo.core.domain.PaymentGatewayClient;
import com.nexsol.cargo.core.domain.PgApprovalResult;
import com.nexsol.cargo.core.domain.PgCancelResult;
import com.nexsol.cargo.core.enums.ResultCode;
import com.nexsol.cargo.core.support.Sha256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NicePaymentsClient implements PaymentGatewayClient {

	private final WebClient.Builder webClientBuilder;

	private final Sha256Util sha256Util;

	private final ObjectMapper objectMapper;

	@Value("${nice-pay.mid}")
	private String mid;

	@Value("${nice-pay.merchant-key}")
	private String merchantKey;

	private static final DateTimeFormatter EDI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	private static final Charset EUC_KR = Charset.forName("EUC-KR");

	private static final String CANCEL_SUCCESS_CODE = "2001";

	private static final String KEY_IN_URL = "https://webapi.nicepay.co.kr/webapi/card_keyin.jsp";

	private static final String CANCEL_URL = "https://pg-api.nicepay.co.kr/webapi/cancel_process.jsp"; // ⬅️
																										// [신규]

	@Override
	public PgApprovalResult approve(String txTid, String authToken, BigDecimal amount, String mid, String NextAppURL) {
		String ediDate = LocalDateTime.now().format(EDI_DATE_FORMATTER);
		String amtString = amount.setScale(0, RoundingMode.DOWN).toPlainString();

		String signDataPlain = authToken + mid + amtString + ediDate + merchantKey;
		String signData = sha256Util.sha(signDataPlain);

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("TID", txTid);
		formData.add("AuthToken", authToken);
		formData.add("MID", mid);
		formData.add("Amt", amtString);
		formData.add("EdiDate", ediDate);
		formData.add("SignData", signData);
		formData.add("EdiType", "JSON");
		formData.add("CharSet", "euc-kr");

		log.info("[NicePayments] 승인 요청 시작. TID: {}, URL: {}", txTid, NextAppURL);

		WebClient webClient = webClientBuilder.build();

		String responseString = null;
		try {
			responseString = webClient.post()
				.uri(NextAppURL)
				.contentType(new MediaType("application", "x-www-form-urlencoded", EUC_KR))
				.body(BodyInserters.fromValue(formData))
				.retrieve()
				.bodyToMono(byte[].class) // ⬅️ Map.class가 아님
				.map(bytes -> new String(bytes, EUC_KR)) // ⬅️ EUC-KR로 수동 디코딩
				.block();

			log.info("[NicePayments] 승인 응답 수신 (Raw): {}", responseString);

			Map<String, String> responseMap = objectMapper.readValue(responseString, new TypeReference<>() {
			});

			if (responseMap == null || responseMap.get("ResultCode") == null) {
				log.error("[NicePayments] 승인 API 응답이 null이거나 ResultCode가 없습니다. TID: {}", txTid);
				throw new RuntimeException("PG사 승인 API 응답 오류");
			}

			String resultCode = responseMap.get("ResultCode");

			if (ResultCode.findByCode(resultCode) != null) {
				log.info("[NicePayments] 승인 성공 TID: {}, ResultCode: {}", txTid, resultCode);
				String authCode = responseMap.get("AuthCode");
				String cardCode = responseMap.get("CardCode");

				return new PgApprovalResult(authCode, cardCode != null ? cardCode : "");

			}
			else {
				String resultMessage = responseMap.get("ResultMsg");
				log.warn("[NicePayments] 승인 실패. TID: {}, ResultCode: {}, ResultMessage: {}", txTid, resultCode,
						resultMessage);
				throw new RuntimeException("PG사 승인 실패: " + resultMessage);
			}

		}
		catch (Exception e) {
			log.error("[NicePayments] 승인 응답 파싱 실패 (HTML 수신 의심). Raw Response: {}", responseString, e);

			throw new RuntimeException("PG사 응답 처리 실패 (Raw: " + responseString + ")", e);
		}

	}

	@Override
	public void netCancel(String txTid, String authToken, BigDecimal amount, String mid, String NetCancelURL) {
		String ediDate = LocalDateTime.now().format(EDI_DATE_FORMATTER);
		String amtString = amount.toPlainString();

		String signDataPlain = authToken + mid + amtString + ediDate + merchantKey;
		String signData = sha256Util.sha(signDataPlain);

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("TID", txTid);
		formData.add("AuthToken", authToken);
		formData.add("MID", mid);
		formData.add("Amt", amtString);
		formData.add("EdiDate", ediDate);
		formData.add("SignData", signData);
		formData.add("NetCancel", "1");
		formData.add("EdiType", "JSON");
		formData.add("CharSet", "euc-kr");

		log.warn("[NicePayments] 승인 실패로 인한 망취소 요청 시작. TID: {}, URL: {}", txTid, NetCancelURL);

		try {
			WebClient webClient = webClientBuilder.build();
			Map<String, String> responseMap = webClient.post()
				.uri(NetCancelURL)
				.contentType(new MediaType("application", "x-www-form-urlencoded", EUC_KR))
				.body(BodyInserters.fromValue(formData))
				.retrieve()
				.bodyToMono(Map.class)
				.block();

			if (responseMap != null && CANCEL_SUCCESS_CODE.equals(responseMap.get("ResultCode"))) {
				log.info("[NicePayments] 망취소 성공. TID: {}", txTid);
			}
			else {
				log.error("[NicePayments] 망취소 '실패'. TID: {}, 응답: {}", txTid, responseMap);
			}
		}
		catch (Exception e) {
			log.error("[NicePayments] 망취소 API 호출 중 심각한 오류 발생. TID: {}", txTid, e);
		}

	}

	@Override
	public PgApprovalResult keyInPayment(String tid, String moid, String amt, String encData, String signData,
			String ediDate, String goodsName) {
		// String amtString = amt.toPlainString();
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

		log.info("[NicePayments] Key-In 요청 시작. TID: {}, Moid: {}", tid, moid);

		WebClient webClient = webClientBuilder.build();

		// EUC-KR로 인코딩된 HTML/JSON을 올바르게 디코딩하기 위한 설정
		// Map.class 대신 String.class로 받고, 수동으로 파싱합니다.
		String responseString = null;

		try {
			responseString = webClient.post()
				.uri(KEY_IN_URL)
				.contentType(new MediaType("application", "x-www-form-urlencoded", EUC_KR))
				.body(BodyInserters.fromValue(formData))
				.retrieve()
				.bodyToMono(byte[].class)
				.map(bytes -> new String(bytes, EUC_KR))
				.block();

			log.info("[NicePayments] Key-In 응답 수신 (Raw): {}", responseString);

			Map<String, String> responseMap = objectMapper.readValue(responseString, new TypeReference<>() {
			});

			String resultCode = responseMap.get("ResultCode");
			if ("3001".equals(resultCode)) {
				log.info("[NicePayments] Key-In 성공. TID: {}, Moid: {}", tid, moid);
				String authCode = responseMap.get("AuthCode");
				String cardCode = responseMap.get("CardCode");
				return new PgApprovalResult(authCode, cardCode != null ? cardCode : "");
			}
			else {
				String resultMsg = responseMap.get("ResultMsg");
				log.warn("[NicePayments] Key-In 실패 (JSON 응답). TID: {}, ResultCode: {}, ResultMsg: {}", tid, resultCode,
						resultMsg);
				throw new RuntimeException("카드 결제 실패: " + resultMsg);
			}

		}
		catch (Exception e) {
			log.error("[NicePayments] Key-In 응답 파싱 실패 (HTML 수신 의심). Raw Response: {}", responseString, e);
			throw new RuntimeException("PG사 응답 처리 실패 (Raw: " + responseString + ")", e);
		}
	}

	@Override
	public PgCancelResult cancel(String tid, String mid, String moid, String cancelAmt, String reason, String ediDate,
			String signData) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("TID", tid);
		formData.add("MID", mid);
		formData.add("Moid", moid);
		formData.add("CancelAmt", cancelAmt);
		formData.add("CancelMsg", reason); // 취소 사유
		formData.add("PartialCancelCode", "0"); // 0: 전체취소
		formData.add("EdiDate", ediDate);
		formData.add("SignData", signData);
		formData.add("EdiType", "JSON");
		formData.add("CharSet", "euc-kr");

		log.info("[NicePayments] 승인 취소 요청 시작. TID: {}, Moid: {}", tid, moid);

		WebClient webClient = webClientBuilder.build();
		String responseString = null;

		try {
			responseString = webClient.post()
				.uri(CANCEL_URL)
				.contentType(new MediaType("application", "x-www-form-urlencoded", EUC_KR))
				.body(BodyInserters.fromValue(formData))
				.retrieve()
				.bodyToMono(byte[].class)
				.map(bytes -> new String(bytes, EUC_KR))
				.block();

			log.info("[NicePayments] 승인 취소 응답 (Raw): {}", responseString);
			Map<String, String> responseMap = objectMapper.readValue(responseString, new TypeReference<>() {
			});

			String resultCode = responseMap.get("ResultCode");
			if (CANCEL_SUCCESS_CODE.equals(resultCode)) {
				log.info("[NicePayments] 승인 취소 성공. TID: {}", tid);
				return new PgCancelResult(resultCode, responseMap.get("ResultMsg"), responseMap.get("CancelAmt"),
						responseMap.get("CancelDate"), responseMap.get("CancelTime"));
			}
			else {
				String resultMsg = responseMap.get("ResultMsg");
				log.warn("[NicePayments] 승인 취소 실패. TID: {}, ResultCode: {}, ResultMsg: {}", tid, resultCode, resultMsg);
				throw new RuntimeException("PG 취소 실패: " + resultMsg);
			}

		}
		catch (Exception e) {
			log.error("[NicePayments] 승인 취소 응답 파싱 실패. Raw: {}", responseString, e);
			throw new RuntimeException("PG 취소 응답 처리 실패", e);
		}
	}

}