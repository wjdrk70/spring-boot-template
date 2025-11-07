package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.ConveyanceType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CreateQuotation(Long userId, String hsCode, BigDecimal invoiceAmount, String currencyUnit,
		BigDecimal exchangeRateAmount, // TODO: 현재는 OCR단계에서 받지 않으나, 플랜 계산을 위해 필요 추후 제거
		String refNo, String blNo, LocalDate outboundDate, String origin, String destination, ConveyanceType conveyance,
		String packingType, String cargoItemName

) {
}
