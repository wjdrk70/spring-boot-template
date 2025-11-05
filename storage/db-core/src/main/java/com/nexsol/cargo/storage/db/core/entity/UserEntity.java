package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

	@Column(name = "company_code", unique = true, nullable = false)
	private String companyCode;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public static UserEntity fromDomain(User user) {
		UserEntity entity = new UserEntity();
		entity.companyCode = user.getCompanyCode();
		entity.password = user.getPassword();
		entity.role = user.getRole();

		return entity;
	}

	public User toDomain() {
		return User.builder()
			.id(this.getId())
			.companyCode(this.companyCode)
			.password(this.password)
			.role(this.role)
			.build();
	}

}
