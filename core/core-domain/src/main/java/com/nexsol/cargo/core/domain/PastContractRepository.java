package com.nexsol.cargo.core.domain;

import java.util.List;

public interface PastContractRepository {

	List<PastContractCoverage> findByHsCode(String hsCode);

}
