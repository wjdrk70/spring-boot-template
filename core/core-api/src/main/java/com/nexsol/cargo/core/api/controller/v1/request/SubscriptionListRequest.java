package com.nexsol.cargo.core.api.controller.v1.request;

import com.nexsol.cargo.core.domain.SubscriptionSearch;
import com.nexsol.cargo.core.support.DomainPageRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionListRequest {

	private int page = 0;

	private int size = 10;

	private String keyword;

	private LocalDate startDate = LocalDate.now().minusMonths(1);

	private LocalDate endDate = LocalDate.now();

	public DomainPageRequest toPageRequest() {
		return new DomainPageRequest(page, size);
	}

	public SubscriptionSearch toSubscriptionSearch() {
		return new SubscriptionSearch(keyword, startDate, endDate);

	}

}
