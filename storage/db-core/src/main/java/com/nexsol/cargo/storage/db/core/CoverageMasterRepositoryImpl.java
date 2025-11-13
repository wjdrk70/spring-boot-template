package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.BaseCoverage;
import com.nexsol.cargo.core.domain.CoverageMaster;
import com.nexsol.cargo.core.domain.CoverageMasterRepository;
import com.nexsol.cargo.core.domain.OptionCoverage;
import com.nexsol.cargo.storage.db.core.entity.CoverageOptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CoverageMasterRepositoryImpl implements CoverageMasterRepository {

	private final CoverageBaseJpaRepository coverageBaseJpaRepository;

	private final CoverageOptionJpaRepository coverageOptionJpaRepository;

	@Override
	public CoverageMaster findCoveragesByCode(Set<String> code) {
		BaseCoverage baseCoverage = coverageBaseJpaRepository.findByCodeIn(code)
			.map(entity -> entity.toDomain())
			.orElse(null);

		// '옵션 담보' 조회
		List<OptionCoverage> options = coverageOptionJpaRepository.findByCodeIn(code)
			.stream()
			.map(CoverageOptionEntity::toDomain)
			.collect(Collectors.toList());

		return new CoverageMaster(baseCoverage, options);
	}

}
