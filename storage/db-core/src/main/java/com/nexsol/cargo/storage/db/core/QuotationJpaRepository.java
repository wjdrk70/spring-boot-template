package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.QuotationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuotationJpaRepository extends JpaRepository<QuotationEntity, Long> {

	Optional<QuotationEntity> findByQuotationKey(String quotationKey);

}
