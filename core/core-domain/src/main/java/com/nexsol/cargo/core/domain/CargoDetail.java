package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConveyanceType;

import java.time.LocalDate;

public record CargoDetail(String refNo, String blNo, LocalDate outboundDate, String origin, String destination,
		ConveyanceType conveyance, String packingType, String cargoDetailName) {
}
