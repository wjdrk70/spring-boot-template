package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.CreateSubscriptionRequest;
import com.nexsol.cargo.core.api.controller.v1.request.SaveSignatureRequest;
import com.nexsol.cargo.core.api.controller.v1.response.CreateSubscriptionResponse;
import com.nexsol.cargo.core.api.controller.v1.response.PolicyResponse;
import com.nexsol.cargo.core.api.controller.v1.response.SignatureUrlResponse;
import com.nexsol.cargo.core.api.controller.v1.response.SubscriptionContractResponse;
import com.nexsol.cargo.core.api.support.error.CoreApiErrorType;
import com.nexsol.cargo.core.api.support.error.CoreApiException;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.api.support.response.PageResponse;
import com.nexsol.cargo.core.domain.*;
import com.nexsol.cargo.core.support.DomainPage;
import com.nexsol.cargo.core.support.DomainPageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	@GetMapping("/{subscriptionId}/signature/image")
	public ResponseEntity<byte[]> getSignatureImage(@AuthenticationPrincipal Long userId,
			@PathVariable Long subscriptionId) {

		SubscriptionSignatureImage result = subscriptionService.getSignatureImage(userId, subscriptionId);

		String contentType = result.contentType();

		String extension = contentType.substring(contentType.indexOf("/") + 1);

		return ResponseEntity.ok()

			.contentType(MediaType.parseMediaType(contentType))
			.header(HttpHeaders.CONTENT_DISPOSITION,
					"inline; filename=\"signature-" + subscriptionId + "." + extension + "\"")
			.body(result.imageBytes());
	}

	@GetMapping("/{subscriptionId}/signature/url")

	public ApiResponse<SignatureUrlResponse> getSignatureDownloadUrl(@AuthenticationPrincipal Long userId,
			@PathVariable Long subscriptionId) {

		String presignedUrl = subscriptionService.getSignatureDownloadUrl(userId, subscriptionId);

		return ApiResponse.success(SignatureUrlResponse.from(presignedUrl));
	}

	@PostMapping
	public ApiResponse<CreateSubscriptionResponse> create(@AuthenticationPrincipal Long userId,
			@Valid @RequestBody CreateSubscriptionRequest request) {
		CreateSubscription dto = request.toCreateSubscription(userId);

		SubscriptionResult result = subscriptionService.create(dto);

		return ApiResponse.success(CreateSubscriptionResponse.of(result));

	}

	@PostMapping("/{subscriptionId}/issue")
	public ApiResponse<PolicyResponse> issuePolicy(@AuthenticationPrincipal Long userId,
			@PathVariable Long subscriptionId) {

		Subscription subscription = subscriptionService.issuePolicy(userId, subscriptionId);

		return ApiResponse.success(PolicyResponse.of(subscription));

	}

	@PostMapping("/{subscriptionId}/signature")
	public ApiResponse<Object> saveSignature(@AuthenticationPrincipal Long userId, @PathVariable Long subscriptionId,
			@Valid @RequestBody SaveSignatureRequest request) {
		if (!subscriptionId.equals(request.getSubscriptionId())) {
			throw new CoreApiException(CoreApiErrorType.INVALID_REQUEST);
		}
		subscriptionService.saveSignature(userId, subscriptionId, request.getSignatureBase64(),
				request.getContentType());
		return ApiResponse.success();
	}

	@GetMapping
	public ApiResponse<PageResponse<SubscriptionContractResponse>> getMySubscriptions(
			@AuthenticationPrincipal Long userId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		DomainPageRequest pageRequest = new DomainPageRequest(page, size);

		DomainPage<SubscriptionContract> domainPage = subscriptionService.getMyContracts(userId, pageRequest);

		List<SubscriptionContractResponse> responseContent = domainPage.content()
			.stream()
			.map(SubscriptionContractResponse::of)
			.toList();

		return ApiResponse.success(PageResponse.of(domainPage, responseContent));
	}

}