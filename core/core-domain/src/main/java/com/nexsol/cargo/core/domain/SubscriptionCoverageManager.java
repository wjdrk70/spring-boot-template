package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConditionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionCoverageManager {

	private final CoverageMasterReader masterReader;

	public List<SubscriptionCoverage> create(Set<String> coverageCodes) {
		CoverageMaster masterSet = masterReader.read(coverageCodes);

		BaseCoverage base = masterSet.baseCoverage();
		SubscriptionCoverage baseSnapshot = new SubscriptionCoverage(ConditionType.BASE, base.getCode(),
				base.getName());
		List<SubscriptionCoverage> optionSnapshots = masterSet.options()
			.stream()
			.map(option -> new SubscriptionCoverage(
					option.getOptionType().name().equals("REFERENCE") ? ConditionType.REFERENCE
							: option.getOptionType().name().equals("ADDITIONAL") ? ConditionType.ADDITIONAL
									: ConditionType.EXTENSION,
					option.getCode(), option.getName()))
			.collect(Collectors.toList());

		optionSnapshots.add(0, baseSnapshot);
		return optionSnapshots;
	}

}
