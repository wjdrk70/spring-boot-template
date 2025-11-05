package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.domain.CargoDetail;
import com.nexsol.cargo.core.enums.ConveyanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CargoDetailRequest {

	@NotBlank(message = "Ref No는 필수입니다.")
	private String refNo;

	@NotBlank(message = "B/L No는 필수입니다.")
	private String blNo;

	@NotNull(message = "출발일자는 필수입니다.")
	private LocalDate outboundDate;

	@NotBlank(message = "출발지는 필수입니다.")
	private String origin;

	@NotBlank(message = "도착지는 필수입니다.")
	private String destination;

	@NotNull(message = "운송용구는 필수입니다.")
	private ConveyanceType conveyance;

	@NotBlank(message = "포장 구분은 필수입니다.")
	private String packingType;

	@NotBlank(message = "품목 상세는 필수입니다.")
	private String cargoDetailName;

	public CargoDetail toDomain() {
		return new CargoDetail(refNo, blNo, outboundDate, origin, destination, conveyance, packingType,
				cargoDetailName);
	}

}
