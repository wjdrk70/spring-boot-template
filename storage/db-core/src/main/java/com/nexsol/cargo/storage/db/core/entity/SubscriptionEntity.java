package com.nexsol.cargo.storage.db.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Table(name = "subscription")
@Entity
@NoArgsConstructor
public class SubscriptionEntity extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name="status")
    private String status;

    @Column(name="insurance_premium")
    private BigDecimal insurancePremium;

    @Column(name="is_same")
    private Boolean isSame;

    @Column(name="policyholder_company_name")
    private String policyholderCompanyName;

    @Column(name="policyholder_company_code")
    private String policyholderCompanyCode;

    @Column(name="insured_company_name")
    private String insuredCompanyName;

    @Column(name="insured_company_code")
    private String insuredCompanyCode;

    @Column(name = "hs_code") // 추가된 컬럼
    private String hsCode;

    @Column(name="ocr_data_snapshot",columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String ocrDataSnapshot;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubscriptionSnapshotEntity> snapshots;
}