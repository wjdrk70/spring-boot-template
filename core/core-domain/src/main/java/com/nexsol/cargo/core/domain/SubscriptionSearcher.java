package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.support.DomainPage;
import com.nexsol.cargo.core.support.DomainPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionSearcher {

	private final SubscriptionRepository subscriptionRepository;

	public SubscriptionSummery search(Long userId, SubscriptionSearch contract, DomainPageRequest pageRequest) {
		return subscriptionRepository.searchByContract(userId, contract, pageRequest);
	}

}
