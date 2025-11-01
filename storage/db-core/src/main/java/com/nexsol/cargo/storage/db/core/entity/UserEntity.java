package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

	@Column(name = "login_id", unique = true, nullable = false)
	private String loginId;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	// UserEntity는 UserProfile의 주인이 아님!!!!!!!!!!
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private UserProfileEntity profile;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private CompanyInfoEntity companyInfo;

	public static UserEntity fromDomain(User user) {
		UserEntity entity = new UserEntity();
		entity.loginId = user.getLoginId();
		entity.password = user.getPassword();
		entity.role = user.getRole();
		return entity;
	}

	public User toDomain() {
		return User.builder()
			.loginId(this.loginId)
			.password(this.password)
			.role(this.role)
			.profile(this.profile != null ? this.profile.toDomain() : null)
			.companyInfo(this.companyInfo != null ? this.companyInfo.toDomain() : null)
			.build();
	}

}
