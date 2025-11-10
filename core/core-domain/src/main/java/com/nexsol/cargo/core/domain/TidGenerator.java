package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.TidMediumType;
import com.nexsol.cargo.core.enums.TidPaymentMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TidGenerator {

	@Value("${nice-pay.mid}")
	private String mid;

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

	public String generateTid() {
		String paymentMethod = TidPaymentMethod.CARD.getCode();
		String mediumType = TidMediumType.GENERAL.getCode();
		String timeInfo = LocalDateTime.now().format(DATE_FORMATTER);

		int randomNum = ThreadLocalRandom.current().nextInt(0, 10000);
		String random = String.format("%04d", randomNum);

		StringBuilder tidBuilder = new StringBuilder(30);
		tidBuilder.append(this.mid);
		tidBuilder.append(paymentMethod);
		tidBuilder.append(mediumType);
		tidBuilder.append(timeInfo);
		tidBuilder.append(random);

		return tidBuilder.toString();
	}

}
