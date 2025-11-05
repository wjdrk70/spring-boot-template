package com.nexsol.cargo.core.api.controller.v1;


import com.nexsol.cargo.core.api.controller.v1.request.CreatePaymentRequest;
import com.nexsol.cargo.core.api.controller.v1.response.CreatePaymentResponse;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.domain.PaymentReadyResult;
import com.nexsol.cargo.core.domain.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;


    @PostMapping("/v1/payments")
    public ApiResponse<CreatePaymentResponse> create(@AuthenticationPrincipal Long userId,
                                                     @Valid @RequestBody CreatePaymentRequest request){
        PaymentReadyResult result = paymentService.createPayment(
                userId,
                request.getSubscriptionId()
        );

        return ApiResponse.success(CreatePaymentResponse.fromDomain(result));
    }

    @PostMapping("/v1/payments/callback/success")
    public ApiResponse<Object> callbackSuccess(
            // (PG사가 전달하는 파라미터들)
            @RequestParam("TID") String tid,
            @RequestParam("AuthToken") String authToken,
            @RequestParam("Amt") BigDecimal amount,
            @RequestParam("MID") String mid
            // ... (기타 주문/인증 정보)
    ) {
        // 1. [Presentation] 'Business Layer'에 작업 위임
        paymentService.approvePayment(tid, authToken, amount, mid);

        return ApiResponse.success();
    }

    @PostMapping("/v1/payments/callback/fail")
    public ApiResponse<Object> callbackFail(
            @RequestParam("TID") String tid,
            @RequestParam("ResultCode") String code,
            @RequestParam("ResultMsg") String message
            // ... (기타 주문 정보)
    ) {
        // 1. [Presentation] 'Business Layer'에 작업 위임
        paymentService.failPayment(tid, code, message);

        return ApiResponse.success();
    }

}
