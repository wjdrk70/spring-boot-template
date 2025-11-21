package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConveyanceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CoverageSetFilter {

	private static final Set<String> AIR_ONLY_CODES = Set.of("101645", "101644");

	public List<SubscriptionCoverageSet> filter(List<SubscriptionCoverageSet> sets, ConveyanceType type) {
		if (sets == null || sets.isEmpty()) {
			return List.of();
		}

		if (type == ConveyanceType.SHIP) {
			log.info("Filtering coverage sets for SHIP transport: Removing AIR-only coverages.");
			// isNotAirCoverage는 AIR 코드가 없으면 true를 반환 -> AIR 코드가 없는 것만 남김
			return sets.stream().filter(set -> isNotAirCoverage(set.getCoverageCodes())).collect(Collectors.toList());
		}

		log.info("No filtering applied for {} transport.", type);
		return sets;
	}

	private boolean isNotAirCoverage(Set<String> codes) {
		log.debug("Checking if AIR-only coverages are present in {}", codes);
		return codes.stream().noneMatch(AIR_ONLY_CODES::contains);
	}

}
