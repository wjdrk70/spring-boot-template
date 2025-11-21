package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.PaymentMethod;
import com.nexsol.cargo.core.enums.SubscriptionStatus;
import com.nexsol.cargo.core.support.DomainPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SubscriptionContractMapper {

	private final PaymentReader paymentReader;

	public SubscriptionSummery<SubscriptionContract> map(SubscriptionSummery summery) {
		DomainPage<Subscription> subscriptionPage = summery.page();

		if (subscriptionPage.content().isEmpty()) {
			return new SubscriptionSummery(DomainPage.empty(), summery.totalPremium());
		}

		List<Long> ids = subscriptionPage.content().stream().map(Subscription::getId).toList();
		Map<Long, PaymentMethod> paymentMethodMap = paymentReader.readMethodsBySubscriptionIds(ids);

		List<SubscriptionContract> contracts = subscriptionPage.content().stream().map(sub -> {
			PaymentMethod method = paymentMethodMap.get(sub.getId());
			return mapToContract(sub, method);
		}).toList();

		DomainPage<SubscriptionContract> contractPage = new DomainPage<>(contracts, subscriptionPage.totalElements(),
				subscriptionPage.totalPages(), subscriptionPage.currentPage(), subscriptionPage.hasNext());

		return new SubscriptionSummery(contractPage, summery.totalPremium());
	}

	private SubscriptionContract mapToContract(Subscription sub, PaymentMethod method) {
		LocalDate outboundDate = (sub.getCargoDetail() != null) ? sub.getCargoDetail().outboundDate() : null;

		LocalDate endDate;
		if (sub.getStatus() == SubscriptionStatus.CANCEL && sub.getCanceledAt() != null) {

			endDate = sub.getCanceledAt().toLocalDate();
		}
		else {

			endDate = LocalDate.now();
		}
		String paymentMethodName = (method != null) ? method.getTitle() : "-";

		return new SubscriptionContract(sub.getId(), sub.getPolicyNumber(), sub.getStatus(),
				sub.getPolicyholderCompanyName(), sub.getPolicyholderCompanyCode(), sub.getManagerName(),
				sub.getManagerPhone(), sub.getCreatedAt() != null ? sub.getCreatedAt().toLocalDate() : null,
				outboundDate, endDate, sub.getInsurancePremium(), paymentMethodName);
	}

}
