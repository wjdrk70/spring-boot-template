package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.domain.UserRepository;
import com.nexsol.cargo.storage.db.core.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(User user) {
		UserEntity entity = UserEntity.fromDomain(user);
		UserEntity savedEntity = userJpaRepository.save(entity);
		return savedEntity.toDomain();
	}

	@Override
	public Optional<User> findByCompanyCode(String companyCode) {
		return userJpaRepository.findByCompanyCode(companyCode).map(UserEntity::toDomain);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userJpaRepository.findById(id).map(UserEntity::toDomain);
	}

}
