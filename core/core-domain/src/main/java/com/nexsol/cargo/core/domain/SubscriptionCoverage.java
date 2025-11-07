package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConditionType;
import lombok.Builder;

@Builder
public record SubscriptionCoverage(ConditionType conditionType, String conditionCode, String conditionName) {
}
