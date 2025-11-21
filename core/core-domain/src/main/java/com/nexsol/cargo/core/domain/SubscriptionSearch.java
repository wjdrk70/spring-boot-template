package com.nexsol.cargo.core.domain;

import java.time.LocalDate;

public record SubscriptionSearch(String keyword, LocalDate startDate, LocalDate endDate) {
}
