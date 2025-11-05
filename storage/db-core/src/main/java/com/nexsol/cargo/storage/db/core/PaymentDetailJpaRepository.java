package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.storage.db.core.entity.PaymentDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailJpaRepository extends JpaRepository<PaymentDetailEntity, Long> {

}
