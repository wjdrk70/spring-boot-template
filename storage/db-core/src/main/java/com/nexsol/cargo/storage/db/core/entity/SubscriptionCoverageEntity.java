package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.CoverageSnapshot;
import com.nexsol.cargo.core.enums.ConditionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_coverage")
@Getter
@NoArgsConstructor
public class SubscriptionCoverageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

	@Column(name = "condition_code", nullable = false)
	private String conditionCode;

	@Column(name = "condition_name", nullable = false)
	private String conditionName;

	@Enumerated(EnumType.STRING)
	@Column(name = "condition_type", nullable = false)
	private ConditionType conditionType;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

    public static SubscriptionCoverageEntity fromDomain(CoverageSnapshot snapshot, Long newSubscriptionId) {
        SubscriptionCoverageEntity entity = new SubscriptionCoverageEntity();
        entity.subscriptionId = newSubscriptionId;
        entity.conditionType = snapshot.conditionType();
        entity.conditionCode = snapshot.conditionCode();
        entity.conditionName = snapshot.conditionName();
        entity.createdAt = LocalDateTime.now();
        return entity;
    }
    public CoverageSnapshot toDomain() {
        return new CoverageSnapshot(this.conditionType, this.conditionCode, this.conditionName);
    }
}
