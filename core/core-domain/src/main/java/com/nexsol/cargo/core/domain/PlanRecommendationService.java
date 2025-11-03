package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlanRecommendationService {
    private final PastContractRepository pastContractRepository;
    private final CoverageMaterRepository coverageMaterRepository;
    private final PremiumCalculator premiumCalculator;
    private final PlanAnalyzer planAnalyzer;


    public List<RecommendPlan> recommendPlans(String hsCode, BigDecimal invoiceAmount) {

        // 1. [격벽] hs_code로 과거 계약 담보 조합(들)을 가져옴 (변경 없음)
        List<PastContractCoverage> contracts = pastContractRepository.findByHsCode(hsCode);

        // 2. [협력자] 빈도수 분석 Top 3 조합(코드 Set)을 찾음 (변경 없음)
        List<Set<String>> topCoverageSets = planAnalyzer.getTopFrequentCoverageSets(contracts, 3);

        List<RecommendPlan> recommendedPlans = new ArrayList<>();
        int planRank = 0;

        for (Set<String> codeSet : topCoverageSets) {

            // 3a. [격벽] 담보 코드 Set으로 '기본'/'옵션' 분리된 마스터 정보를 가져옴 (수정됨)
            CoverageMaster masterSet = coverageMaterRepository.findCoveragesByCode(codeSet);

            // 3b. [격벽] 분리된 모델로 보험료를 계산 (수정됨)
            BigDecimal premium = premiumCalculator.calculate(
                    invoiceAmount,
                    masterSet.baseCoverage(),
                    masterSet.options()
            );

            // 3c. [조립] 최종 'RecommendedPlan' 객체 생성 (수정됨)
            String planName = "플랜 " + (char) ('A' + planRank++);
            recommendedPlans.add(new RecommendPlan(
                    planName,
                    masterSet.baseCoverage(),
                    masterSet.options(),
                    premium
            ));
        }

        return recommendedPlans;
    }
}
