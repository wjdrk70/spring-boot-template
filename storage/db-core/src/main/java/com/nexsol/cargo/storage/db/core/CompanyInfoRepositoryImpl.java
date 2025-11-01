package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.CompanyInfo;
import com.nexsol.cargo.core.domain.CompanyInfoRepository;
import com.nexsol.cargo.storage.db.core.entity.CompanyInfoEntity;
import com.nexsol.cargo.storage.db.core.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyInfoRepositoryImpl implements CompanyInfoRepository {

	private final CompanyInfoJpaRepository companyInfoJpaRepository;

	private final UserJpaRepository userJpaRepository;

	@Override
	public CompanyInfo save(Long userId, CompanyInfo companyInfo) {
		UserEntity userEntity = userJpaRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		CompanyInfoEntity entity = CompanyInfoEntity.fromDomain(companyInfo, userEntity);
		CompanyInfoEntity savedEntity = companyInfoJpaRepository.save(entity);

		return savedEntity.toDomain();
	}

}
