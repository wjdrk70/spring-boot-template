package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.CompanyInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_info")
@Getter
@NoArgsConstructor
public class CompanyInfoEntity extends BaseEntity {

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "company_name", nullable = false)
	private String companyName;

	@Column(name = "company_code", nullable = false, length = 50)
	private String companyCode;

	@Column(name = "manager_name", nullable = false)
	private String managerName;

	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Column(nullable = false)
	private String email;

	@Embedded
	private AddressEmbeddable address;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public static CompanyInfoEntity fromDomain(CompanyInfo companyInfo, UserEntity user) {
		CompanyInfoEntity entity = new CompanyInfoEntity();

		entity.user = user;
		entity.companyName = companyInfo.getCompanyName();
		entity.companyCode = companyInfo.getCompanyCode();
		entity.managerName = companyInfo.getManagerName();
		entity.phoneNumber = companyInfo.getPhoneNumber();
		entity.email = companyInfo.getEmail();
		entity.address = AddressEmbeddable.fromDomain(companyInfo.getAddress());

		return entity;
	}

	public CompanyInfo toDomain() {
		return CompanyInfo.builder()
			.companyName(this.companyName)
			.companyCode(this.companyCode)
			.managerName(this.managerName)
			.phoneNumber(this.phoneNumber)
			.email(this.email)
			.address(this.address != null ? this.address.toDomain() : null)
			.build();
	}

}
