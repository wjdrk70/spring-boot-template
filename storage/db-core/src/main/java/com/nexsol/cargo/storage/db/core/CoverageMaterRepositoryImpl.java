package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.BaseCoverage;
import com.nexsol.cargo.core.domain.CoverageMaster;
import com.nexsol.cargo.core.domain.CoverageMaterRepository;
import com.nexsol.cargo.core.domain.OptionCoverage;
import com.nexsol.cargo.storage.db.core.entity.CoverageOptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CoverageMaterRepositoryImpl implements CoverageMaterRepository {
    private final CoverageRiderJpaRepository coverageRiderJpaRepository;
    private final CoverageOptionJpaRepository coverageOptionJpaRepository;

    @Override
    public CoverageMaster findCoveragesByCode(Set<String> code) {
        BaseCoverage baseCoverage = coverageRiderJpaRepository.findByCodeIn(code)
                .map(entity -> entity.toDomain())
                .orElse(null); // (실제로는 orElseThrow 예외 처리 필요)

        // 2. [격벽 너머의 구현] JpaRepository를 사용해 '옵션 담보' 조회
        List<OptionCoverage> options = coverageOptionJpaRepository.findByCodeIn(code).stream()
                .map(CoverageOptionEntity::toDomain)
                .collect(Collectors.toList());

        // 3. 'Port'를 통해 약속된 DTO(Record) 형태로 조립하여 반환
        return new CoverageMaster(baseCoverage, options);
    }
}
