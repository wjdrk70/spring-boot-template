package com.nexsol.cargo.core.api.controller.v1.response;

import com.nexsol.cargo.core.domain.CargoDetail;
import com.nexsol.cargo.core.enums.ConveyanceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CargoInfoResponse {

	private final String refNo;

	private final String blNo;

	private final BigDecimal invoiceAmount;

	private final String currencyUnit;

	private final LocalDate outboundDate;

	private final String origin;

	private final String destination;

	private final ConveyanceType conveyance;

	private final String packingType;

	private final String cargoItemName;

	public static CargoInfoResponse of(CargoDetail detail) {
		if (detail == null) {
			return null;
		}
		return new CargoInfoResponse(detail.refNo(), detail.blNo(), detail.invoiceAmount(), detail.currencyUnit(),
				detail.outboundDate(), detail.origin(), detail.destination(), detail.conveyance(), detail.packingType(),
				detail.cargoItemName());
	}

}