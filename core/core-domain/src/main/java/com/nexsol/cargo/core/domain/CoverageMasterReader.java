package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CoverageMasterReader {

	private final CoverageMasterRepository coverageMasterRepository;

	public CoverageMaster findValidatedMaster(Set<String> codeSet) {

		CoverageMaster masterSet = coverageMasterRepository.findCoveragesByCode(codeSet);

		// 필수 담보(Base Coverage) 누락 검증
		if (masterSet.baseCoverage() == null) {

			throw new CoreException(CoreErrorType.NOT_FOUND_DATA);
		}

		return masterSet;
	}

}
