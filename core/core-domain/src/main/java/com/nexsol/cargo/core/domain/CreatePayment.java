package com.nexsol.cargo.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePayment {

	private String authResultCode;

	private String moid;

	private String amt;

	private String txTid;

	private String authToken;

	private String mid;

	private String signature;

	private String nextAppURL;

	private String netCancelURL;

}
