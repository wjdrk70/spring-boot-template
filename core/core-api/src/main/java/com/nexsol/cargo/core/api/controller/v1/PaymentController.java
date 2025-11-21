package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.CancelPaymentRequest;
import com.nexsol.cargo.core.api.controller.v1.request.CreateKeyInPaymentRequest;
import com.nexsol.cargo.core.api.controller.v1.request.CreatePaymentRequest;
import com.nexsol.cargo.core.api.controller.v1.request.PaymentRequest;
import com.nexsol.cargo.core.api.controller.v1.response.CreatePaymentResponse;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.domain.PaymentReadyResult;
import com.nexsol.cargo.core.domain.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@Value("${app.payment.success-redirect-url}")
	private String successRedirectUrl;

	@Value("${app.payment.dev-success-redirect-url}")
	private String devSuccessRedirectUrl;

	@PostMapping("/v1/payments")
	public ApiResponse<CreatePaymentResponse> create(@AuthenticationPrincipal Long userId,
			@Valid @RequestBody CreatePaymentRequest request) {
		PaymentReadyResult result = paymentService.createPayment(userId, request.getSubscriptionId());

		return ApiResponse.success(CreatePaymentResponse.fromDomain(result));
	}

	@PostMapping("/v1/payments/{paymentId}/cancel")
	public ApiResponse<Object> cancelPayment(@AuthenticationPrincipal Long userId, @PathVariable Long paymentId,
			@Valid @RequestBody CancelPaymentRequest request) {
		paymentService.cancelPayment(paymentId, userId, request.getReason());
		return ApiResponse.success();
	}

	@PostMapping("/v1/payments/key-in")
	public ApiResponse<Object> createKeyInPayment(@AuthenticationPrincipal Long userId,
			@Valid @RequestBody CreateKeyInPaymentRequest request) {
		paymentService.createKeyInPayment(request.toKeyInPayment(userId));
		return ApiResponse.success();
	}

	@PostMapping("/v1/payments/callback/success")
	public ResponseEntity<Void> callbackSuccess(@ModelAttribute PaymentRequest request,
			@RequestHeader(value = "Host") String host) {
		String finalRedirectUrl;

		paymentService.approvePayment(request.toCreatePayment());

		if (host.equals("localhost") || host.equals("127.0.0.1")) {
			finalRedirectUrl = devSuccessRedirectUrl;
		}
		else {
			finalRedirectUrl = successRedirectUrl;
		}

		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, finalRedirectUrl).build();
	}

	@PostMapping("/v1/payments/callback/fail")
	public ApiResponse<Object> callbackFail(@RequestParam("TID") String tid, @RequestParam("ResultCode") String code,
			@RequestParam("ResultMsg") String message
	// ... (기타 주문 정보)
	) {
		// 1. [Presentation] 'Business Layer'에 작업 위임
		paymentService.failPayment(tid, code, message);

		return ApiResponse.success();
	}

}
