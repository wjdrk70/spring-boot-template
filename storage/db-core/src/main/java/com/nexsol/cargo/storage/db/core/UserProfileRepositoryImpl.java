package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.UserProfile;
import com.nexsol.cargo.core.domain.UserProfileRepository;
import com.nexsol.cargo.storage.db.core.entity.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepository {

	private final UserProfileJpaRepository userProfileJpaRepository;

	@Override
	public UserProfile save(UserProfile profile) {
		UserProfileEntity entity = UserProfileEntity.fromDomain(profile);
		UserProfileEntity savedEntity = userProfileJpaRepository.save(entity);
		return savedEntity.toDomain();
	}

	@Override
	public Optional<UserProfile> findByUserId(Long userId) {
		return userProfileJpaRepository.findById(userId).map(UserProfileEntity::toDomain);
	}

}
