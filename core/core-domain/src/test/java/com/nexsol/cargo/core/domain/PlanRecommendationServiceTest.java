package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConveyanceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@DisplayName("PlanRecommendationService (보험 플랜 추천 서비스) 테스트")
class PlanRecommendationServiceTest {

	@Mock
	private SubscriptionCoverageReader subscriptionCoverageReader;

	@Mock
	private CoverageSetFilter coverageSetFilter;

	@Mock
	private PlanGenerator planGenerator;

	@Mock
	private PlanAnalyzer planAnalyzer;

	@Mock
	private QuotationReader quotationReader;

	@Mock
	private CargoItemFinder cargoItemFinder;

	@Mock
	private Quotation quotation;

	@InjectMocks
	private PlanRecommendationService planRecommendationService;

	@Test
	@DisplayName("성공: 과거_계약_분석_및_플랜_계산_로직을_PlanProcessor에_위임해야_한다")
	void successful_recommendation_to_plan_processor() {
		// given
		String quotationKey = "q-test-key";
		String type = "AIR";
		String hsCode = "4140";
		String middleCode = "30301";
		int topN = 3;

		when(quotationReader.read(quotationKey)).thenReturn(quotation);
		when(quotation.getHsCode()).thenReturn(hsCode);
		when(quotation.getConveyance()).thenReturn(ConveyanceType.SHIP);

		when(cargoItemFinder.find(hsCode)).thenReturn(middleCode);

		List<SubscriptionCoverageSet> coverageSets = List.of(new SubscriptionCoverageSet(Set.of("106018", "106017")));
		List<SubscriptionCoverageSet> filteredCoverageSets = coverageSets;

		when(subscriptionCoverageReader.readCoverageSet(middleCode)).thenReturn(coverageSets);
		when(coverageSetFilter.filter(eq(coverageSets), eq(ConveyanceType.SHIP))).thenReturn(filteredCoverageSets);

		List<Set<String>> topCoverageSets = List.of(Set.of("106018", "106017"));
		when(planAnalyzer.getTopFrequentCoverageSets(filteredCoverageSets, topN)).thenReturn(topCoverageSets);

		List<RecommendPlan> expectedPlans = List.of(new RecommendPlan("플랜 A", null, null, new BigDecimal("21090.00")),
				new RecommendPlan("플랜 B", null, null, new BigDecimal("19000.00")));

		when(planGenerator.generatePlan(eq(topCoverageSets), eq(quotation))).thenReturn(expectedPlans);

		// when
		List<RecommendPlan> recommendedPlans = planRecommendationService.recommendPlans(quotationKey);

		// then
		assertThat(recommendedPlans).usingRecursiveComparison().isEqualTo(expectedPlans);

		verify(quotationReader).read(quotationKey);
		verify(quotation).getConveyance();
		verify(cargoItemFinder).find(hsCode);
		verify(subscriptionCoverageReader).readCoverageSet(middleCode);
		verify(planAnalyzer).getTopFrequentCoverageSets(filteredCoverageSets, topN);
		verify(planGenerator).generatePlan(topCoverageSets, quotation);
	}

	@Test
	@DisplayName("예외: PastContactReader가_빈_리스트를_반환하면_빈_플랜을_반환해야_한다")
	void empty_contract_should_return_empty_plans() {
		// given
		String quotationKey = "q-empty-key";
		String hsCode = "999999";
		String middleCode = "99999";

		List<SubscriptionCoverageSet> emptyList = Collections.emptyList();

		when(quotationReader.read(quotationKey)).thenReturn(quotation);
		when(quotation.getHsCode()).thenReturn(hsCode);
		when(quotation.getConveyance()).thenReturn(ConveyanceType.AIR);

		when(cargoItemFinder.find(hsCode)).thenReturn(middleCode);

		when(subscriptionCoverageReader.readCoverageSet(middleCode)).thenReturn(Collections.emptyList());

		when(coverageSetFilter.filter(eq(emptyList), eq(ConveyanceType.AIR))).thenReturn(emptyList);

		// when
		List<RecommendPlan> recommendedPlans = planRecommendationService.recommendPlans(quotationKey);

		// then
		assertThat(recommendedPlans).isEmpty();

		verify(quotationReader).read(quotationKey);
		verify(quotation).getConveyance();
		verify(coverageSetFilter).filter(eq(emptyList), eq(ConveyanceType.AIR));
		verify(cargoItemFinder).find(hsCode);
		verify(subscriptionCoverageReader).readCoverageSet(eq(middleCode));
		verify(planAnalyzer).getTopFrequentCoverageSets(Collections.emptyList(), 3); // planAnalyzer는
		// 호출됨
		verify(planGenerator, never()).generatePlan(any(), any()); // planGenerator는 호출되지
		// 않음
	}

}
