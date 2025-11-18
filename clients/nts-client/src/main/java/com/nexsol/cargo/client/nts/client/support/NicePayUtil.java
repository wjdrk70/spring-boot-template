package com.nexsol.cargo.client.nts.client.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import com.nexsol.cargo.core.support.Sha256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NicePayUtil {

	private final WebClient.Builder webClientBuilder;

	private final Sha256Util sha256Util;

	private final ObjectMapper objectMapper;

	private static final DateTimeFormatter EDI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	private static final Charset EUC_KR = Charset.forName("EUC-KR");

	public Map<String, String> sendRequest(String url, MultiValueMap<String, String> formData) {
		String responseString = webClientBuilder.build()
			.post()
			.uri(url)
			.contentType(new MediaType("application", "x-www-form-urlencoded", EUC_KR))
			.body(BodyInserters.fromValue(formData))
			.retrieve()
			.bodyToMono(byte[].class)
			.map(bytes -> new String(bytes, EUC_KR))
			.block();

		log.info("[NicePayments] 응답 (Raw): {}", responseString);

		try {
			// TypeReference를 사용하여 타입 안전성 확보
			return objectMapper.readValue(responseString, new TypeReference<Map<String, String>>() {
			});
		}
		catch (Exception e) {
			log.error("[NicePayments] 응답 파싱 실패. Raw Response: {}", responseString, e);
			throw new CoreException(CoreErrorType.PAYMENT_AUTH_FAILED);
		}
	}

	private static final String RESULT_CODE_APPROVE_SUCCESS = "3001";

	public void validateResponse(Map<String, String> responseMap, String tid) {
		if (responseMap == null || responseMap.get("ResultCode") == null) {
			log.error("[NicePayments] 응답 오류 TID: {}", tid);
			throw new CoreException(CoreErrorType.PAYMENT_AUTH_FAILED);
		}
		String resultCode = responseMap.get("ResultCode");
		String resultMsg = responseMap.get("ResultMsg");

		// (키인 결제 등 다른 성공 코드가 있다면 || 로 추가)
		if (!RESULT_CODE_APPROVE_SUCCESS.equals(resultCode)) {
			log.warn("[NicePayments] PG 승인 실패. TID: {}, Code: {}, Msg: {}", tid, resultCode, resultMsg);
			// RuntimeException 대신 PG 오류 메시지를 포함하는 CoreException을 던지는 것이 좋습니다.
			// (예: throw new CoreException(CoreErrorType.PAYMENT_AUTH_FAILED, resultMsg);)
			throw new RuntimeException("PG 결제 승인 실패: " + resultMsg);
		}
	}

	public String makeSignature(String plainText) {
		return sha256Util.sha(plainText);
	}

	public String getEdiDate() {
		return LocalDateTime.now().format(EDI_DATE_FORMATTER);
	}

}
