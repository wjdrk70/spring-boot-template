package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.QuotationStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationReader {

	private final QuotationRepository quotationRepository;

	public Quotation read(String quotationKey) {
		Quotation quotation = quotationRepository.findByQuotationKey(quotationKey)
			.orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));

		if (quotation.getStatus() != QuotationStatus.PENDING) {
			throw new CoreException(CoreErrorType.NOT_FOUND_DATA);
		}

		return quotation;

	}

}
