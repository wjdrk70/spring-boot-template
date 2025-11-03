package com.nexsol.cargo.core.domain;

import java.util.Set;

public interface CoverageMaterRepository {

    CoverageMaster findCoveragesByCode(Set<String> code);
}
