package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	private final SubscriptionReader subscriptionReader;

	private final PaymentAppender paymentAppender;

	private final SubscriptionRepository subscriptionRepository;

	private final PaymentGatewayClient paymentGatewayClient;

	@Transactional
	public PaymentReadyResult createPayment(Long userId, Long subscriptionId) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		// TODO: PG 연결 필요

		BigDecimal premium = subscription.getInsurancePremium();

		Payment savedPayment = paymentAppender.appendReady(subscriptionId, premium);

		return new PaymentReadyResult(String.valueOf(savedPayment.getId()), savedPayment.getInsurancePremium(),
				"TODO:_SignData");

	}

	@Transactional
	public void approvePayment(String tid, String authToken, BigDecimal amount, String mid) {
		Payment payment = paymentRepository.findById(Long.parseLong(tid)) // TODO: PG 연동시
																			// 수정
			.orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));

		if (payment.getInsurancePremium().compareTo(amount) != 0) {
			throw new CoreException(CoreErrorType.NOT_FOUND_DATA); // TODO:PAYMENT_AMOUNT_MISMATCH
																	// 추가
		}

		try {
			PgApprovalResult pgResult = paymentGatewayClient.approve(tid, authToken, amount, mid);
			payment.success(pgResult.authCode(), pgResult.cardCode());
			paymentRepository.save(payment);

			Subscription subscription = subscriptionReader.read(payment.getSubscriptionId(), null);
			subscription.completePayment();
			subscriptionRepository.save(subscription);
		}
		catch (Exception e) {
			paymentGatewayClient.netCancel(tid, authToken, amount, mid);

			throw e; // Controller로 예외 전파
		}
	}

	@Transactional
	public void failPayment(String tid, String code, String message) {
		Payment payment = paymentRepository.findById(Long.parseLong(tid)) // TODO: PG 연동시
																			// 수정
			.orElse(null);

		if (payment != null) {
			// TODO: PG 연동 내부 로직 구현
		}
	}

}
