package com.nexsol.cargo.core.support;

import com.nexsol.cargo.core.domain.PaymentCompleteEvent;
import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.domain.SubscriptionRepository;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
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

	@EventListener
	@Transactional
	public void handlePaymentCompleted(PaymentCompleteEvent event) {
		if (event == null || event.subscriptionId() == null) {
			log.warn("[Event] PaymentCompletedEvent 수신 실패. 이벤트 또는 SubscriptionId가 null입니다.");
			return;
		}

		try {
			Subscription subscription = subscriptionRepository.findById(event.subscriptionId())
				.orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA)); // TODO:
																						// 추후
																						// subscription
																						// 에러타입
																						// 추가

			subscription.completePayment();
			subscriptionRepository.save(subscription);
		}
		catch (Exception e) {
			log.error("[Event] PaymentCompletedEvent 처리 중 오류 발생. Subscription ID: {}. Error: {}",
					event.subscriptionId(), e.getMessage(), e);
		}
	}

}
