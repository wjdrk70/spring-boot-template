package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.UserProfile;
import com.nexsol.cargo.core.domain.UserProfileRepository;
import com.nexsol.cargo.storage.db.core.entity.UserEntity;
import com.nexsol.cargo.storage.db.core.entity.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepository {

	private final UserProfileJpaRepository userProfileJpaRepository;

	private final UserJpaRepository userJpaRepository;

	@Override
	public UserProfile save(Long userId, UserProfile profile) {
		UserEntity userEntity = userJpaRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		UserProfileEntity entity = UserProfileEntity.fromDomain(profile, userEntity);
		UserProfileEntity savedEntity = userProfileJpaRepository.save(entity);

		return savedEntity.toDomain();
	}

}
