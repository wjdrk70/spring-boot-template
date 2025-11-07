package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuotationAppender {

	private final QuotationRepository quotationRepository;

	public Quotation append(CreateQuotation creation) {
		String quotationKey = "q-" + UUID.randomUUID().toString().substring(0, 13);

		Quotation quotation = Quotation.createPending(creation, quotationKey);

		return quotationRepository.save(quotation);
	}

}
