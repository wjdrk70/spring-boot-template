package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.PlanRequest;
import com.nexsol.cargo.core.api.controller.v1.response.PlanResponse;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.domain.PlanRecommendationService;
import com.nexsol.cargo.core.domain.RecommendPlan;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanRecommendationService planRecommendationService;

    @PostMapping("/recommend")
    public ApiResponse<List<PlanResponse>> recommendPlans(
            @Valid @RequestBody PlanRequest request) {

        // 1. [Presentation Layer]가 [Business Layer(Domain Service)]를 호출
        List<RecommendPlan> recommendedPlans = planRecommendationService.recommendPlans(
                request.hsCode(),
                request.invoiceAmount()
        );

        // 2. Domain 객체를 Response DTO로 변환하여 반환
        List<PlanResponse> response = recommendedPlans.stream()
                .map(PlanResponse::fromDomain)
                .collect(Collectors.toList());

        return ApiResponse.success(response);
    }
}