package com.nexsol.cargo.core.api.controller.v1.request;

import com.nexsol.cargo.core.domain.CreatePayment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {

	private String AuthResultCode;

	private String AuthResultMsg;

	private String AuthToken;

	private String PayMethod;

	private String MID;

	private String Moid;

	private String Signature;

	private String Amt;

	private String TxTid;

	private String NextAppURL;

	private String NetCancelURL;

	private String EdiDate;

	public CreatePayment toCreatePayment() {
		return CreatePayment.builder()
			.authResultCode(this.AuthResultCode)
			.moid(this.Moid)
			.amt(this.Amt)
			.txTid(this.TxTid)
			.authToken(this.AuthToken)
			.mid(this.MID)
			.signature(this.Signature)
			.nextAppURL(this.NextAppURL)
			.netCancelURL(this.NetCancelURL)
			.ediDate(this.EdiDate)
			.build();
	}

}
