package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_snapshot")
@Getter
@NoArgsConstructor
public class SubscriptionSnapshotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private SubscriptionEntity subscription;

    @Column(name = "condition_code", nullable = false)
    private String conditionCode;

    // (필요시 'condition_type', 'condition_name' 등 다른 컬럼도 추가)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 편의를 위해 subscription_id를 직접 반환하는 getter
    public Long getSubscriptionId() {
        return (subscription != null) ? subscription.getId() : null;
    }
}
