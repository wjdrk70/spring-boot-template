package com.nexsol.cargo.core.domain;

public record PgCancelResult(String resultCode, String resultMsg, String cancelAmt, String cancelDate,
		String cancelTime) {
}
