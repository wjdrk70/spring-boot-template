package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
	private String name;

	public static UserProfileEntity fromDomain(UserProfile profile, UserEntity user) {
		UserProfileEntity entity = new UserProfileEntity();
		entity.user = user;
		entity.name = profile.getName();
		return entity;
	}

	public UserProfile toDomain() {
		return UserProfile.builder().name(this.name).build();
	}

}
