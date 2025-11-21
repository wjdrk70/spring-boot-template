package com.nexsol.cargo.core.api.controller.v1.response;

import lombok.Builder;

@Builder
public record SignatureUrlResponse(String downloadUrl) {
	public static SignatureUrlResponse from(String url) {
		return new SignatureUrlResponse(url);
	}
}