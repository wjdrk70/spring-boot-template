package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionCoverageReader {

	private final SubscriptionCoverageRepository subscriptionCoverageRepository;

	public List<SubscriptionCoverageSet> readCoverageSet(String hsCode) {
		// 과거 계약 담보 조합
		return subscriptionCoverageRepository.findByHsCode(hsCode);

	}

}
