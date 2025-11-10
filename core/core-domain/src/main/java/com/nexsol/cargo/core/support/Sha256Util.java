package com.nexsol.cargo.core.support;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class Sha256Util {

	private static final Charset EUC_KR = Charset.forName("EUC-KR");

	private static final HexFormat hexFormat = HexFormat.of().withLowerCase();

	public String sha(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// 3. [수정] UTF_8이 아닌 EUC-KR 바이트로 해시
			byte[] hash = digest.digest(base.getBytes(EUC_KR));

			// 4. Java 17+ HexFormat으로 변환
			return hexFormat.formatHex(hash);

		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다.", e);
		}
	}

}
