package com.nexsol.cargo.core.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CompanyInfoRepository {

	CompanyInfo save(Long userId, CompanyInfo companyInfo);

}
