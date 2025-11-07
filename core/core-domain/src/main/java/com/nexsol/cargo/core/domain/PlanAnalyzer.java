package com.nexsol.cargo.core.domain;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PlanAnalyzer {

	/**
	 * 과거 계약 리스트에서 가장 빈도수가 높은 N개의 담보 조합(Set)을 반환
	 */
	public List<Set<String>> getTopFrequentCoverageSets(List<SubscriptionCoverageSet> contracts, int topN) {
		if (contracts == null || contracts.isEmpty()) {
			return Collections.emptyList();
		}

		// '담보 코드 Set'별로 그룹화하고 카운트
		Map<Set<String>, Long> frequencyMap = contracts.stream()
			.map(SubscriptionCoverageSet::getCoverageCodes)
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		// 카운트(Value) 기준 내림차순 정렬
		return frequencyMap.entrySet()
			.stream()
			.sorted(Map.Entry.<Set<String>, Long>comparingByValue().reversed())
			.limit(topN)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}

}
