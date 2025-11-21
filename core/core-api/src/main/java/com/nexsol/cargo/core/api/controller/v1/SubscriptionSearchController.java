package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.SubscriptionListRequest;
import com.nexsol.cargo.core.api.controller.v1.response.SubscriptionContractResponse;
import com.nexsol.cargo.core.api.controller.v1.response.SubscriptionListResponse;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.api.support.response.PageResponse;
import com.nexsol.cargo.core.domain.SubscriptionContract;
import com.nexsol.cargo.core.domain.SubscriptionService;
import com.nexsol.cargo.core.support.DomainPage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/subscription/search")
@RequiredArgsConstructor
public class SubscriptionSearchController {

	private final SubscriptionService subscriptionService;

	@GetMapping
	public ApiResponse<SubscriptionListResponse> search(@AuthenticationPrincipal Long userId,
			@ModelAttribute SubscriptionListRequest request) {

		var summery = subscriptionService.searchMyContracts(userId, request.toSubscriptionSearch(),
				request.toPageRequest());

		return ApiResponse.success(SubscriptionListResponse.of(summery));
	}

}
