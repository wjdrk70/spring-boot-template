package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CargoItemFinder {

	private final CargoItemRepository cargoItemRepository;

	public String find(String hsCode) {
		String middleCode = cargoItemRepository.findMiddleCodeByHsCode(hsCode)
			.orElseThrow(() -> new CoreException(CoreErrorType.CARGO_ITEM_NOT_FOUND));

		return middleCode;
	}

}
