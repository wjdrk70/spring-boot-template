package com.nexsol.cargo.core.api.support.error;

import com.nexsol.cargo.core.error.CoreErrorType;

public record CoreApiErrorMessage(String code, String message, Object data) {

	public CoreApiErrorMessage(CoreErrorType coreErrorType, Object data) {
		this(coreErrorType.getCode().name(), coreErrorType.getMessage(), data);
	}
}