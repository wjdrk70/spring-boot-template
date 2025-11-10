package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentMethod;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import com.nexsol.cargo.core.support.CardEncryptor;
import com.nexsol.cargo.core.support.Sha256Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

	private final SubscriptionRepository subscriptionRepository;

	private final PaymentGatewayClient paymentGatewayClient;

	private static final String AUTH_RESULT_CODE_SUCCESS = "0000";

	private final ApplicationEventPublisher eventPublisher;

	private final Sha256Util sha256Util;

	private final CardEncryptor cardEncryptor;

	@Value("${nice-pay.merchant-key}")
	private String merchantKey;

	@Value("${nice-pay.mid}")
	private String mid;

	private static final DateTimeFormatter EDI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	@Transactional
	public PaymentReadyResult createPayment(Long userId, Long subscriptionId) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		BigDecimal premium = subscription.getInsurancePremium();

		Payment savedPayment = paymentAppender.appendReady(subscriptionId, premium, PaymentMethod.SIMPLE_PAY);

		String moid = String.valueOf(savedPayment.getId());
		String amt = premium.toPlainString();
		String ediDat = LocalDateTime.now().format(EDI_DATE_FORMATTER);
		String plainText = this.mid + moid + amt + ediDat + this.merchantKey;
		String signData = sha256Util.sha(plainText);

		return new PaymentReadyResult(moid, this.mid, amt, ediDat, signData);

	}

	@Transactional
	public void createKeyInPayment(KeyInPayment keyInPayment) {
		Subscription subscription = subscriptionReader.read(keyInPayment.getSubscriptionId(), keyInPayment.getUserId());
		BigDecimal premium = subscription.getInsurancePremium();
		String amt = premium.setScale(0, RoundingMode.DOWN).toPlainString();

		String goodsName = subscription.getCargoDetail().cargoItemName();
		if (goodsName == null || goodsName.isBlank()) {
			goodsName = "보험료 결제"; // (비상시 기본값)
		}

		Payment payment = paymentAppender.appendReady(keyInPayment.getSubscriptionId(), premium,
				PaymentMethod.CARD_KEY_IN);
		String moid = String.valueOf(payment.getId());

		String tid = tidGenerator.generateTid();
		log.info("tid: {}", tid);
		String ediDate = LocalDateTime.now().format(EDI_DATE_FORMATTER);

		String encData = cardEncryptor.encrypt(keyInPayment.getCardNo(), keyInPayment.getCardExpireYYMM(),
				keyInPayment.getBuyerAuthNum(), keyInPayment.getCardPwd());

		String signDataPlain = this.mid + amt + ediDate + moid + this.merchantKey;
		String signData = sha256Util.sha(signDataPlain);
		log.info("signData: {}", signData);
		// PG Client 즉시 승인 호출 ---
		// (키인 API는 콜백(approve)이 없고 즉시 승인/실패가 결정됨)
		try {
			PgApprovalResult pgResult = paymentGatewayClient.keyInPayment(tid, moid, amt, encData, signData, ediDate,
					goodsName);

			payment.success(pgResult.authCode(), pgResult.cardCode());
			paymentRepository.save(payment);

			eventPublisher.publishEvent(new PaymentCompleteEvent(payment.getSubscriptionId()));

		}
		catch (Exception e) {
			// PG Client에서 승인 실패(3001 외) 시 예외를 던짐
			// 키인 API는 '망취소'가 없으므로, 즉시 Controller로 예외 전파
			throw e;
		}

	}

	@Transactional
	public void approvePayment(CreatePayment createPayment) {
		if (!AUTH_RESULT_CODE_SUCCESS.equals(createPayment.getAuthResultCode())) {
			throw new CoreException(CoreErrorType.PAYMENT_AUTH_FAILED);
		}

		String hashing = createPayment.getAuthToken() + createPayment.getMid() + createPayment.getAmt() + merchantKey;
		String expectedSignature = sha256Util.sha(hashing);

		if (!expectedSignature.equals(createPayment.getSignature())) {
			throw new CoreException(CoreErrorType.PAYMENT_SIGNATURE_MISMATCH);
		}

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
			payment.success(pgResult.authCode(), pgResult.cardCode());
			paymentRepository.save(payment);

			Subscription subscription = subscriptionReader.read(payment.getSubscriptionId(), null);
			subscription.completePayment();
			subscriptionRepository.save(subscription);
		}
		catch (Exception e) {
			paymentGatewayClient.netCancel(createPayment.getTxTid(), createPayment.getAuthToken(), requestedAmount,
					createPayment.getMid(), createPayment.getNetCancelURL());

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
