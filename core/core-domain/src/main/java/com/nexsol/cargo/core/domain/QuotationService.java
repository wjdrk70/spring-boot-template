package com.nexsol.cargo.core.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationService {

	private final QuotationAppender quotationAppender;

	@Transactional
	public String createQuotation(CreateQuotation creation) {
		Quotation quotation = quotationAppender.append(creation);

		return quotation.getQuotationKey();
	}

}
