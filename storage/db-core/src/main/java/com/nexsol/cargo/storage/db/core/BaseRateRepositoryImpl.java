package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.BaseRateRepository;
import com.nexsol.cargo.storage.db.core.entity.BaseRateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BaseRateRepositoryImpl implements BaseRateRepository {

	private final BaseRateJpaRepository baseRateJpaRepository;

	@Override
	public Optional<BigDecimal> findRate(String middleCode, String baseCoverageCode, String voyageCode) {
		// TODO: BaseRateJpaRepository에 쿼리 메서드 추가 필요
		// Spring Data JPA의 기본 'findAll'을 사용한 임시 필터링 로직 (성능에 좋지 않음)
		// return baseRateJpaRepository.findAll()
		// .stream()
		// .filter(rate -> rate.getMiddleCode().equals(middleCode)
		// && rate.getBaseCoverageCode().equals(baseCoverageCode) &&
		// rate.getVoyageCode().equals(voyageCode))
		// .map(BaseRateEntity::getRate)
		// .findFirst();

		return baseRateJpaRepository
			.findByMiddleCodeAndBaseCoverageCodeAndVoyageCode(middleCode, baseCoverageCode, voyageCode)
			.map(BaseRateEntity::getRate); // Optional<BaseRateEntity>를
											// Optional<BigDecimal>로 변환

	}

}
