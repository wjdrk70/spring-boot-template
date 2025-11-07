package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConveyanceType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CargoDetail(String hsCode, BigDecimal invoiceAmount, String currencyUnit, String refNo, String blNo,
		LocalDate outboundDate, String origin, String destination, ConveyanceType conveyance, String packingType,
		String cargoItemName) {
}