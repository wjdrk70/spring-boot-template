package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profile")
@Getter
@NoArgsConstructor
public class UserProfileEntity extends BaseEntity {

	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String companyName;

	@Column(nullable = false)
	private String managerName;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String email;

	@Embedded
	private AddressEmbeddable address;

	public static UserProfileEntity fromDomain(UserProfile profile, UserEntity user) {
		UserProfileEntity entity = new UserProfileEntity();
		entity.user = user;
		entity.companyName = profile.getCompanyName();
		entity.managerName = profile.getManagerName();
		entity.phoneNumber = profile.getPhoneNumber();
		entity.email = profile.getEmail();
		entity.userName = profile.getUserName();
		entity.address = AddressEmbeddable.fromDomain(profile.getAddress());
		return entity;
	}

	public UserProfile toDomain() {
		return UserProfile.builder()
			.userName(this.userName)
			.companyName(this.companyName)
			.managerName(this.managerName)
			.phoneNumber(this.phoneNumber)
			.email(this.email)
			.address(this.address.toDomain())
			.build();
	}

}
