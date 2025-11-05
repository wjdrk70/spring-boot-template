package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "subscription")
@Entity
@NoArgsConstructor
public class SubscriptionEntity extends BaseEntity {

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private SubscriptionStatus status;

	@Column(name = "is_same")
	private Boolean isSame;

	@Column(name = "policyholder_company_name")
	private String policyholderCompanyName;

	@Column(name = "policyholder_company_code")
	private String policyholderCompanyCode;

	@Column(name = "insured_company_name")
	private String insuredCompanyName;

	@Column(name = "insured_company_code")
	private String insuredCompanyCode;


	public static SubscriptionEntity fromDomain(Subscription domain) {
		SubscriptionEntity entity = new SubscriptionEntity();

        entity.userId = domain.userId();
        entity.status = domain.status();
        entity.isSame = domain.isSame();
        entity.policyholderCompanyName = domain.policyholderCompanyName();
        entity.policyholderCompanyCode = domain.policyholderCompanyCode();
        entity.insuredCompanyName = domain.insuredCompanyName();
        entity.insuredCompanyCode = domain.insuredCompanyCode();

		return entity;
	}

//    public Subscription toDomain(CargoDetail cargoDetail, List<CoverageSnapshot> snapshots) {
//        return new Subscription(this.getId(), this.userId, this.status,
//                null,
//                this.isSame, this.policyholderCompanyName, this.policyholderCompanyCode,
//                this.insuredCompanyName, this.insuredCompanyCode,
//                cargoDetail, snapshots);
//    }

}