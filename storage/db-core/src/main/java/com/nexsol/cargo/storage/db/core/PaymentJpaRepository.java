package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

}
