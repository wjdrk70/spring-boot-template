package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.SubscriptionContract;
import com.nexsol.cargo.core.domain.SubscriptionSummery;
import com.nexsol.cargo.core.support.DomainPage;

import java.math.BigDecimal;
import java.util.List;

public record SubscriptionListResponse(List<SubscriptionContractResponse> content, long totalElements, int totalPages,
		int currentPage, boolean hasNext,

		// [핵심] 이 API만을 위한 특화 필드
		BigDecimal totalPremium) {
	public static SubscriptionListResponse of(SubscriptionSummery<SubscriptionContract> summery) {
		DomainPage<SubscriptionContract> page = summery.page();

		List<SubscriptionContractResponse> content = page.content()
			.stream()
			.map(SubscriptionContractResponse::of)
			.toList();

		return new SubscriptionListResponse(content, page.totalElements(), page.totalPages(), page.currentPage(),
				page.hasNext(), summery.totalPremium());
	}
}