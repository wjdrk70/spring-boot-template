package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.CreateQuotationRequest;
import com.nexsol.cargo.core.api.controller.v1.response.CreateQuotationResponse;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.domain.QuotationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/quotation")
@RequiredArgsConstructor
public class QuotationController {

	private final QuotationService quotationService;

	@PostMapping
	public ApiResponse<CreateQuotationResponse> create(@AuthenticationPrincipal Long userId,
			@Valid @RequestBody CreateQuotationRequest request) {

		String quotationKey = quotationService.createQuotation(request.toCreateQuotation(userId));

		return ApiResponse.success(CreateQuotationResponse.from(quotationKey));
	}

}
