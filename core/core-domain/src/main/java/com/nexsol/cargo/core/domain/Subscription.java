package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record Subscription(
        Long id,
        Long userId,
        SubscriptionStatus status,

        boolean isSame,
        String policyholderCompanyName,
        String policyholderCompanyCode,
        String insuredCompanyName,
        String insuredCompanyCode,
        CargoDetail cargoDetail,
        List<CoverageSnapshot> snapshots) {

}
