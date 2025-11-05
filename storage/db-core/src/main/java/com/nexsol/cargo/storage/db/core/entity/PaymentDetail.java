package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "payment_detail")
@Entity
@NoArgsConstructor
public class PaymentDetail extends BaseEntity {

    @OneToOne
    @MapsId
    @JoinColumn(name = "subscription_id")
    private SubscriptionEntity subscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "card_number_masked")
    private String cardNumberMasked;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "external_payment_key")
    private String externalPaymentKey;
}
