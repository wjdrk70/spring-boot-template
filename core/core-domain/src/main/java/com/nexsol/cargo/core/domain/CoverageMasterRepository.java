package com.nexsol.cargo.core.domain;

import java.util.Set;

public interface CoverageMasterRepository {

	CoverageMaster findCoveragesByCode(Set<String> code);

}
