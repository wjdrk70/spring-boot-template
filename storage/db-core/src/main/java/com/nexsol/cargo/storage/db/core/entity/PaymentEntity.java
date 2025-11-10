package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.Payment;
import com.nexsol.cargo.core.enums.PaymentMethod;
import com.nexsol.cargo.core.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Table(name = "payment")
@Entity
@NoArgsConstructor
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "subscription_id", nullable = false)
	private Long subscriptionId;

	@Column(name = "insurance_premium", nullable = false)
	private BigDecimal insurancePremium;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method")
	private PaymentMethod paymentMethod;

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

	public static PaymentEntity fromDomain(Payment domain) {
		PaymentEntity entity = new PaymentEntity();
		entity.id = domain.getId();
		entity.subscriptionId = domain.getSubscriptionId();
		entity.insurancePremium = domain.getInsurancePremium();
		entity.paymentStatus = domain.getPaymentStatus();
		entity.paymentMethod = domain.getPaymentMethod();
		entity.cardType = domain.getCardType();
		entity.cardNumberMasked = domain.getCardNumberMasked();
		entity.expiryDate = domain.getExpiryDate();
		entity.externalPaymentKey = domain.getExternalPaymentKey();
		return entity;
	}

	public Payment toDomain() {
		return Payment.builder()
			.id(this.id)
			.subscriptionId(this.subscriptionId)
			.insurancePremium(this.insurancePremium)
			.paymentStatus(this.paymentStatus)
			.paymentMethod(this.paymentMethod)
			.cardType(this.cardType)
			.cardNumberMasked(this.cardNumberMasked)
			.expiryDate(this.expiryDate)
			.externalPaymentKey(this.externalPaymentKey)
			.build();
	}

}
