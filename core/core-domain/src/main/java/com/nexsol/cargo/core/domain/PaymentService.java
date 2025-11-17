package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentMethod;
import com.nexsol.cargo.core.enums.PaymentStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import com.nexsol.cargo.core.support.CardEncryptor;
import com.nexsol.cargo.core.support.PgUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final Logger log = LoggerFactory.getLogger(PaymentService.class);

	private final TidGenerator tidGenerator;

	private final PaymentRepository paymentRepository;

	private final SubscriptionReader subscriptionReader;

	private final PaymentAppender paymentAppender;

	private final PaymentGatewayClient paymentGatewayClient;

	private final ApplicationEventPublisher eventPublisher;

	private final CardEncryptor cardEncryptor;

	private final PgUtil pgUtil;

	@Value("${nice-pay.mid}")
	private String mid;

	private static final DateTimeFormatter EDI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	private static final String AUTH_RESULT_CODE_SUCCESS = "0000";

	@Transactional
	public PaymentReadyResult createPayment(Long userId, Long subscriptionId) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		BigDecimal premium = subscription.getInsurancePremium();

		Payment savedPayment = paymentAppender.appendReady(subscriptionId, premium, PaymentMethod.SIMPLE_PAY);

		String moid = String.valueOf(savedPayment.getId());
		String amt = pgUtil.format(premium);
		String ediDat = LocalDateTime.now().format(EDI_DATE_FORMATTER);
		String signData = pgUtil.generateSignature(this.mid, amt);

		return new PaymentReadyResult(moid, this.mid, amt, ediDat, signData);

	}

	@Transactional
	public void approvePayment(CreatePayment createPayment) {
		if (!AUTH_RESULT_CODE_SUCCESS.equals(createPayment.getAuthResultCode())) {
			throw new CoreException(CoreErrorType.PAYMENT_AUTH_FAILED);
		}

		pgUtil.verifyApproveSignature(createPayment);

		Long paymentId = Long.parseLong(createPayment.getMoid());
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));

		BigDecimal requestedAmount = new BigDecimal(createPayment.getAmt());
		if (payment.getInsurancePremium().compareTo(requestedAmount) != 0) {
			throw new CoreException(CoreErrorType.PAYMENT_AMOUNT_MISMATCH);
		}

		try {
			PgApprovalResult pgResult = paymentGatewayClient.approve(createPayment.getTxTid(),
					createPayment.getAuthToken(), requestedAmount, this.mid, createPayment.getNextAppURL());

			payment.success(createPayment.getTxTid(), pgResult.authCode(), pgResult.cardCode());
			paymentRepository.save(payment);

			eventPublisher.publishEvent(new PaymentCompleteEvent(payment.getSubscriptionId()));
		}
		catch (Exception e) {
			paymentGatewayClient.netCancel(createPayment.getTxTid(), createPayment.getAuthToken(), requestedAmount,
					createPayment.getMid(), createPayment.getNetCancelURL());

			throw e;
		}

	}

	@Transactional
	public void cancelPayment(Long paymentId, Long userId, String reason) {
		log.info("결제 취소 시도. Payment ID: {}, User ID: {}", paymentId, userId);

		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new CoreException(CoreErrorType.PAYMENT_NOT_FOUND));

		Subscription subscription = subscriptionReader.read(payment.getSubscriptionId(), userId);

		if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
			log.warn("이미 취소되었거나 성공한 결제가 아님. Payment ID: {}", paymentId);
			throw new CoreException(CoreErrorType.PAYMENT_CANCEL_FAILED);
		}

		String tid = payment.getExternalPaymentKey();
		String moid = String.valueOf(payment.getId());
		String cancelAmt = pgUtil.format(payment.getInsurancePremium());
		String ediDate = LocalDateTime.now().format(EDI_DATE_FORMATTER);

		String signData = pgUtil.generateCancelSignature(this.mid, cancelAmt, ediDate);

		try {

			paymentGatewayClient.cancel(tid, this.mid, moid, cancelAmt, reason, ediDate, signData);

			payment.cancel();
			paymentRepository.save(payment);

			eventPublisher.publishEvent(new PaymentCancelEvent(payment.getSubscriptionId()));

		}
		catch (Exception e) {
			log.error("PG 결제 취소 실패. Payment ID: {}. Error: {}", paymentId, e.getMessage(), e);
			// TODO: PG 취소는 실패했으나 DB 롤백이 필요한 경우 RuntimeException으로 전환
			throw new RuntimeException("PG 결제 취소에 실패했습니다.", e);
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

	@Transactional
	public void createKeyInPayment(KeyInPayment keyInPayment) {
		Subscription subscription = subscriptionReader.read(keyInPayment.getSubscriptionId(), keyInPayment.getUserId());
		BigDecimal premium = subscription.getInsurancePremium();
		Payment payment = paymentAppender.appendReady(keyInPayment.getSubscriptionId(), premium,
				PaymentMethod.CARD_KEY_IN);

		String amt = pgUtil.format(premium);
		String goodsName = pgUtil.resolveGoodsName(subscription);
		String moid = String.valueOf(payment.getId());
		String tid = tidGenerator.generateTid();
		String ediDate = LocalDateTime.now().format(EDI_DATE_FORMATTER);
		String encData = cardEncryptor.encrypt(keyInPayment.getCardNo(), keyInPayment.getCardExpireYYMM(),
				keyInPayment.getBuyerAuthNum(), keyInPayment.getCardPwd());
		String signData = pgUtil.generateKeyInSignature(this.mid, amt, ediDate, moid);

		try {
			PgApprovalResult pgResult = paymentGatewayClient.keyInPayment(tid, moid, amt, encData, signData, ediDate,
					goodsName);

			payment.success(tid, pgResult.authCode(), pgResult.cardCode());
			paymentRepository.save(payment);

			eventPublisher.publishEvent(new PaymentCompleteEvent(payment.getSubscriptionId()));

		}
		catch (Exception e) {
			// PG Client에서 승인 실패(3001 외) 시 예외를 던짐
			// 키인 API는 '망취소'가 없으므로, 즉시 Controller로 예외 전파
			throw e;
		}

	}

}
