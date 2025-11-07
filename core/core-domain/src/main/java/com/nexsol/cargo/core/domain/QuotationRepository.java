package com.nexsol.cargo.core.domain;

import java.util.Optional;

public interface QuotationRepository {

	Quotation save(Quotation quotation);

	Optional<Quotation> findByQuotationKey(String quotationKey);

}
