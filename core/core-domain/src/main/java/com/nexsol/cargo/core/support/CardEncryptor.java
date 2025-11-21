package com.nexsol.cargo.core.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Component
public class CardEncryptor {

	@Value("${nice-pay.merchant-key}")
	private String merchantKey;

	private static final String ALGORITHM = "AES";

	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

	private static final HexFormat hexFormat = HexFormat.of().withLowerCase();

	private static final Charset EUC_KR = Charset.forName("EUC-KR");

	public String encrypt(String cardNo, String cardExpireYYMM, String buyerAuthNum, String cardPwd) {
		try {
			String plainText = String.format("CardNo=%s&CardExpire=%s&BuyerAuthNum=%s&CardPwd=%s", cardNo,
					cardExpireYYMM, buyerAuthNum, cardPwd);

			SecretKeySpec secretKeySpec = getAesKey();

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(EUC_KR));

			return hexFormat.formatHex(encryptedBytes);
		}
		catch (Exception e) {
			throw new RuntimeException("카드 정보 암호화 실패", e);
		}
	}

	private SecretKeySpec getAesKey() {
		if (merchantKey == null || merchantKey.length() < 16) {
			throw new IllegalArgumentException("MerchantKey가 16자리 미만입니다.");
		}
		byte[] keyBytes = merchantKey.substring(0, 16).getBytes(EUC_KR);
		return new SecretKeySpec(keyBytes, ALGORITHM);
	}

}
