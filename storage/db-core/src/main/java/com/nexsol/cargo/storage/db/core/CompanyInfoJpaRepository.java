package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.CompanyInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInfoJpaRepository extends JpaRepository<CompanyInfoEntity, Long> {

}
