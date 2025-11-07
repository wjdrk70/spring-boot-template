package com.nexsol.cargo.core.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationEventHandler {

	private static final Logger log = LoggerFactory.getLogger(QuotationEventHandler.class);

	private final QuotationReader quotationReader;

	private final QuotationRepository quotationRepository;

	@Async
	@Transactional
	@EventListener
	public void handleSubscriptionCreate(SubscriptionCreatEvent event) {
		try {
			Quotation quotation = quotationReader.read(event.quotationKey());

			quotation.subscribe();
			quotationRepository.save(quotation);

			log.info("[Event] Quotation {} 상태 SUBSCRIBED로 변경 완료", event.quotationKey());
		}
		catch (Exception e) {
			log.warn("[Event] Quotation 상태 변경 실패 ({}): {}", event.quotationKey(), e.getMessage());

		}
	}

}
