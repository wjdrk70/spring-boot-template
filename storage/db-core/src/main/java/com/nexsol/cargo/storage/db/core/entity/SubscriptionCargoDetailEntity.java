package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.enums.ConveyanceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@Table(name = "subscription_cargo_detail")
@Entity
@NoArgsConstructor
public class SubscriptionCargoDetailEntity extends BaseEntity {

    @OneToOne
    @MapsId // BaseEntity.id에 subscription.id 값을 매핑
    @JoinColumn(name = "subscription_id")
    private SubscriptionEntity subscription;

    @Column(name = "ref_no")
    private String refNo;

    @Column(name = "bl_no")
    private String blNo;

    @Column(name = "outbound_date")
    private LocalDate outboundDate;

    @Column
    private String origin;

    @Column
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column
    private ConveyanceType conveyance;

    @Column(name = "packing_type")
    private String PackingType;

    @Column(name = "cargo_item_name")
    private String cargoItemName;


}
