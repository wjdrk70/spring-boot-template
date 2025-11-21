package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.support.DomainPage;

import java.math.BigDecimal;

public record SubscriptionSummery<T>(DomainPage<T> page, BigDecimal totalPremium) {
}
