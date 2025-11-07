package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.Quotation;
import com.nexsol.cargo.core.domain.QuotationRepository;
import com.nexsol.cargo.storage.db.core.entity.QuotationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuotationRepositoryImpl implements QuotationRepository {

	private final QuotationJpaRepository quotationJpaRepository;

	@Override
	public Quotation save(Quotation quotation) {
		QuotationEntity entity = QuotationEntity.fromDomain(quotation);
		QuotationEntity savedEntity = quotationJpaRepository.save(entity);

		return savedEntity.toDomain();
	}

	@Override
	public Optional<Quotation> findByQuotationKey(String quotationKey) {
		return quotationJpaRepository.findByQuotationKey(quotationKey).map(QuotationEntity::toDomain);
	}

}
