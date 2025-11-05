package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConditionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionSnapshotCreator {

	private final CoverageMasterReader masterReader;

	public List<CoverageSnapshot> create(Set<String> coverageCodes) {
		CoverageMaster masterSet = masterReader.findValidatedMaster(coverageCodes);

		BaseCoverage base = masterSet.baseCoverage();
		CoverageSnapshot baseSnapshot = new CoverageSnapshot(ConditionType.BASE, base.getCode(), base.getName());
		List<CoverageSnapshot> optionSnapshots = masterSet.options()
			.stream()
			.map(option -> new CoverageSnapshot(option.getType().name().equals("REFERENCE") ? ConditionType.REFERENCE
					: option.getType().name().equals("ADDITIONAL") ? ConditionType.ADDITIONAL : ConditionType.EXTENSION,
					option.getCode(), option.getName()))
			.collect(Collectors.toList());

		optionSnapshots.add(0, baseSnapshot);
		return optionSnapshots;
	}

}
