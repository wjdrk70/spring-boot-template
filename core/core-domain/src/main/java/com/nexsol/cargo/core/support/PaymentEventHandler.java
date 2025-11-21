package com.nexsol.cargo.core.support;

import com.nexsol.cargo.core.domain.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventHandler {

	private static final Logger log = LoggerFactory.getLogger(PaymentEventHandler.class);

	private final SubscriptionRepository subscriptionRepository;

	private final SubscriptionReader subscriptionReader;

	private final BucketStorageClient bucketStorageClient;

	@EventListener
	@Transactional
	public void handlePaymentCompleted(PaymentCompleteEvent event) {
		if (event == null || event.subscriptionId() == null) {
			log.warn("[Event] PaymentCompletedEvent 수신 실패. 이벤트 또는 SubscriptionId가 null입니다.");
			return;
		}

		try {
			Subscription subscription = subscriptionReader.read(event.subscriptionId());

			if (subscription.getSignatureBase64Temp() != null) {
				String base64String = subscription.getSignatureBase64Temp();
				String contentType = subscription.getSignatureContentTypeTemp();

				byte[] signatureBytes = java.util.Base64.getDecoder().decode(base64String);
				String signatureKey = bucketStorageClient.uploadSignature(subscription.getId(), signatureBytes,
						contentType);

				subscription.updateSignatureKey(signatureKey);
				subscription.clearSignatureBase64Temp();
			}

			subscription.completePayment();
			subscriptionRepository.save(subscription);
		}
		catch (Exception e) {
			// TODO: bucket fail logic
			log.error("[Event] PaymentCompletedEvent 처리 중 오류 발생. Subscription ID: {}. Error: {}",
					event.subscriptionId(), e.getMessage(), e);

		}
	}

	@EventListener
	@Transactional
	public void handlePaymentCancelled(PaymentCancelEvent event) {
		if (event == null || event.subscriptionId() == null) {
			log.warn("[Event] PaymentCancelledEvent 수신 실패. 이벤트 또는 SubscriptionId가 null입니다.");
			return;
		}

		try {
			// 소유권 검증이 필요 없는 내부 시스템용 read 호출
			Subscription subscription = subscriptionReader.read(event.subscriptionId());

			subscription.cancelPayment();
			subscriptionRepository.save(subscription);

			log.info("[Event] Subscription {} 상태 CANCELLED로 변경 완료", event.subscriptionId());
		}
		catch (Exception e) {
			log.error("[Event] PaymentCancelledEvent 처리 중 오류 발생. Subscription ID: {}. Error: {}",
					event.subscriptionId(), e.getMessage(), e);
		}
	}

}
