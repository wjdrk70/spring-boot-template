package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.PlanRequest;
import com.nexsol.cargo.core.api.controller.v1.response.PlanResponse;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.domain.PlanRecommendationService;
import com.nexsol.cargo.core.domain.RecommendPlan;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/plan")
@RequiredArgsConstructor
public class PlanController {

	private final PlanRecommendationService planRecommendationService;

	@PostMapping("/recommend")
	public ApiResponse<List<PlanResponse>> recommendPlans(@Valid @RequestBody PlanRequest request) {

		List<RecommendPlan> recommendedPlans = planRecommendationService.recommendPlans(request.quotationKey());

		List<PlanResponse> response = recommendedPlans.stream().map(PlanResponse::of).collect(Collectors.toList());

		return ApiResponse.success(response);
	}

}