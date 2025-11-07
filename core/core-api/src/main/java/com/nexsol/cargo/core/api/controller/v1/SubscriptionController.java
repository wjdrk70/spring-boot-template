package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.CreateSubscriptionRequest;
import com.nexsol.cargo.core.api.controller.v1.response.CreateSubscriptionResponse;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.domain.CreateSubscription;
import com.nexsol.cargo.core.domain.SubscriptionResult;
import com.nexsol.cargo.core.domain.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	@PostMapping
	public ApiResponse<CreateSubscriptionResponse> create(@AuthenticationPrincipal Long userId,
			@Valid @RequestBody CreateSubscriptionRequest request) {
		CreateSubscription dto = request.toCreateSubscription(userId);

		SubscriptionResult result = subscriptionService.create(dto);

		return ApiResponse.success(CreateSubscriptionResponse.of(result));

	}

}
