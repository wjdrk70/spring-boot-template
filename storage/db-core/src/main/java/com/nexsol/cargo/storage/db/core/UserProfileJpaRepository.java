package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, Long> {

}