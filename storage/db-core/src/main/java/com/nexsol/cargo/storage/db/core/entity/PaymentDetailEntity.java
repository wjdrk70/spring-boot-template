package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.PaymentDetail;
import com.nexsol.cargo.core.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "payment_detail")
@Entity
@NoArgsConstructor
public class PaymentDetailEntity {

	@Id
	@Column(name = "subscription_id")
	private Long id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "subscription_id")
	private SubscriptionEntity subscription;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatus;

	@Column(name = "payment_method")
	private String paymentMethod;

	@Column(name = "card_type")
	private String cardType;

	@Column(name = "card_number_masked")
	private String cardNumberMasked;

	@Column(name = "expiry_date")
	private String expiryDate;

	@Column(name = "external_payment_key")
	private String externalPaymentKey;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public static PaymentDetailEntity fromDomain(PaymentDetail domain, SubscriptionEntity subscription) {
		PaymentDetailEntity entity = new PaymentDetailEntity();
		entity.subscription = subscription;
		entity.paymentStatus = domain.paymentStatus();
		entity.paymentMethod = domain.paymentMethod();
		entity.cardType = domain.cardType();
		entity.cardNumberMasked = domain.cardNumberMasked();
		entity.expiryDate = domain.expiryDate();
		return entity;
	}

	public PaymentDetail toDomain() {
		return new PaymentDetail((this.subscription != null) ? this.subscription.getId() : null, this.paymentMethod,
				this.cardType, this.cardNumberMasked, this.expiryDate, this.paymentStatus);
	}

}
