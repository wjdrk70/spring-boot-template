package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.SubscriptionSearchRequest;
import com.nexsol.cargo.core.api.controller.v1.response.SubscriptionContractResponse;
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
	public ApiResponse<PageResponse<SubscriptionContractResponse>> search(@AuthenticationPrincipal Long userId,
			@ModelAttribute SubscriptionSearchRequest request) {
		DomainPage<SubscriptionContract> domainPage = subscriptionService.searchMyContracts(userId,
				request.toSubscriptionSearch(), request.toPageRequest());

		List<SubscriptionContractResponse> responseContent = domainPage.content()
			.stream()
			.map(SubscriptionContractResponse::of)
			.toList();

		return ApiResponse.success(PageResponse.of(domainPage, responseContent));
	}

}
