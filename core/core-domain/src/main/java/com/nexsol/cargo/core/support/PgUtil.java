package com.nexsol.cargo.core.support;

import com.nexsol.cargo.core.domain.CreatePayment;
import com.nexsol.cargo.core.domain.Subscription;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class PgUtil {

	private final Sha256Util sha256Util;

	@Value("${nice-pay.merchant-key}")
	private String merchantKey;

	private static final DateTimeFormatter EDI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public String format(BigDecimal amount) {
		if (amount == null) {
			return "0";
		}
		return amount.setScale(0, RoundingMode.DOWN).toPlainString();
	}

	public String getEdiDate() {
		return LocalDateTime.now().format(EDI_DATE_FORMATTER);
	}

	public String resolveGoodsName(Subscription subscription) {
		String name = subscription.getCargoDetail().cargoItemName();
		return (name == null || name.isBlank()) ? "보험료 결제" : name;
	}

	public void verifyApproveSignature(CreatePayment createPayment) {
		String hashing = createPayment.getAuthToken() + createPayment.getMid() + createPayment.getAmt()
				+ this.merchantKey;

		String expectedSignature = sha256Util.sha(hashing);

		if (!expectedSignature.equals(createPayment.getSignature())) {
			throw new CoreException(CoreErrorType.PAYMENT_SIGNATURE_MISMATCH);
		}
	}

	public String generateSignature(String mid, String amt) {
		String plainText = mid + amt + this.merchantKey;
		return sha256Util.sha(plainText);
	}

	public String generateKeyInSignature(String mid, String amt, String ediDate, String moid) {
		String plainText = mid + amt + ediDate + moid + this.merchantKey;
		return sha256Util.sha(plainText);
	}

	public String generateCancelSignature(String mid, String cancelAmt, String ediDate) {
		String plainText = mid + cancelAmt + ediDate + this.merchantKey;
		return sha256Util.sha(plainText);
	}

}
