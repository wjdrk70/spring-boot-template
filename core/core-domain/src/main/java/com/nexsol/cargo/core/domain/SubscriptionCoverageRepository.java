package com.nexsol.cargo.core.domain;

import java.util.List;

public interface SubscriptionCoverageRepository {

	List<SubscriptionCoverageSet> findByMiddleCode(String middleCode);

}