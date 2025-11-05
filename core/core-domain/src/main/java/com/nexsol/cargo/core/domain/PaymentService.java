package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentStatus;
import com.nexsol.cargo.core.enums.SubscriptionStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionValidator subscriptionValidator;

    private final PaymentGatewayClient paymentGatewayClient;

	@Transactional
	public PaymentReadyResult createPayment(Long userId, Long subscriptionId, BigDecimal exchangeRateAmount) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));

        if (!subscription.userId().equals(userId)) {
            throw new CoreException(CoreErrorType.AUTH_UNAUTHORIZED); // (임시)
        }

        // 3. [Business] '청약' 상태 확인 (이미 결제되었는지)
        if (subscription.status() != SubscriptionStatus.PAYMENT_PENDING) {
            throw new CoreException(CoreErrorType.NOT_FOUND_DATA); // (임시: 이미 처리된 청약)
        }

        CreateSubscription creationDto = new CreateSubscription(
                subscription.userId(),
                subscription.cargoDetail().hsCode(),
                subscription.cargoDetail().invoiceAmount(),
                subscription.cargoDetail().currencyUnit(),
                exchangeRateAmount, // ⬅️ 클라이언트가 '다시' 전달한 환율
                subscription.snapshots().stream().map(CoverageSnapshot::conditionCode).collect(Collectors.toSet()),
                subscription.isSame(),
                subscription.policyholderCompanyName(),
                subscription.policyholderCompanyCode(),
                subscription.insuredCompanyName(),
                subscription.insuredCompanyCode(),
                subscription.cargoDetail()
        );

        //TODO: PG 연결 필요

        BigDecimal premium = subscriptionValidator.validatePremium(creationDto);

        // 5. [Business] '결제' 개념 생성 (DB 저장)
        PaymentDetail paymentToSave = PaymentDetail.builder()
                .subscriptionId(subscriptionId)
                .insurancePremium(premium)
                .paymentStatus(PaymentStatus.READY)
                .build();

        PaymentDetail savedPayment = paymentRepository.save(paymentToSave);


        return new PaymentReadyResult(
                String.valueOf(savedPayment.subscriptionId()),
                savedPayment.insurancePremium(),
                "TODO:_SignData"
        );
	}

    @Transactional
    public void approvePayment(String tid, String authToken, BigDecimal amount, String mid) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));

        // 2. [Implement] 금액 검증
        if (paymentDetail.amount().compareTo(amount) != 0) {
            throw new CoreException(CoreErrorType.NOT_FOUND_DATA); // TODO: PAYMENT_AMOUNT_MISMATCH 추가
        }

        try {
            // 3. [Implement] '격벽'(Port)을 통해 '외부 PG 승인' 요청 (서버 to 서버)
            PgApprovalResult pgResult = paymentGatewayClient.approve(tid, authToken, amount, mid);

            // 4. [Business] '결제' 상태 'SUCCESS'로 변경 (DB 저장)
            paymentDetail.success(pgResult.getAuthCode(), pgResult.getCardCode());
            paymentRepository.save(paymentDetail);

            // 5. [Business] '청약' 상태 'PAYMENT_COMPLETE'로 변경 (DB 저장)
            Subscription subscription = subscriptionReader.read(paymentDetail.subscriptionId());
            subscription.updateStatus(SubscriptionStatus.PAYMENT_COMPLETE);
            subscriptionRepository.save(subscription);

        } catch (Exception e) {
            // 6. [Implement] '승인' 실패 시, '격벽'(Port)을 통해 '망취소' 요청
            paymentGatewayClient.netCancel(tid, authToken, amount, mid);

            // '결제' 상태 'FAILED'로 변경
            paymentDetail.fail(e.getMessage()); // (임시, fail 구현 필요)
            paymentRepository.save(paymentDetail);

            throw e; // Controller로 예외 전파
        }
    }

    @Transactional
    public void failPayment(String tid, String code, String message) {
        // 1. [Implement] TID로 'READY' 상태인 PaymentDetail 조회
        PaymentDetail paymentDetail = paymentRepository.findByTidAndStatus(tid, PaymentStatus.READY)
                .orElse(null); // 실패는 찾지 못해도 로깅만

        if (paymentDetail != null) {
            // 2. [Business] '결제' 상태 'FAILED'로 변경
            paymentDetail.fail(String.format("[%s] %s", code, message));
            paymentRepository.save(paymentDetail);
        }

        // (실패 트랜잭션 로깅)
    }

}
