package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionCoverageReader {

	private final SubscriptionCoverageRepository subscriptionCoverageRepository;

	public List<SubscriptionCoverageSet> readCoverageSet(String middleCode) {

		return subscriptionCoverageRepository.findByMiddleCode(middleCode);

	}

}
