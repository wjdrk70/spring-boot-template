package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConditionType;

public record CoverageSnapshot(ConditionType conditionType, String conditionCode, String conditionName) {
}
