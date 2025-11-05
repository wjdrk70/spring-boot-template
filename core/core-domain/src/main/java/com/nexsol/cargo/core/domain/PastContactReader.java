package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PastContactReader {
    private final PastContractRepository pastContractRepository;
    private final PlanAnalyzer planAnalyzer;

    public List<Set<String>> readTopCombinations(String hsCode, int topN) {
        // 과거 계약 담보 조합
        List<PastContractCoverage> contracts = pastContractRepository.findByHsCode(hsCode);

        // 빈도수 분석 Top N 조합
        return planAnalyzer.getTopFrequentCoverageSets(contracts, topN);
    }
}
