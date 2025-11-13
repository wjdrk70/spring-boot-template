package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CargoItemFinder {

	private final CargoItemRepository cargoItemRepository;

	public String find(String hsCode) {
		String middleCode = cargoItemRepository.findMiddleCodeByHsCode(hsCode)
			.orElseThrow(() -> new IllegalArgumentException("HS코드에 해당하는 품목중분류코드를 찾을 수 없습니다: " + hsCode));

		return middleCode;
	}

}
