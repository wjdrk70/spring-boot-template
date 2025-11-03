package com.nexsol.cargo.web.security;

import com.nexsol.cargo.core.domain.JwtPayload;
import com.nexsol.cargo.core.domain.JwtService;
import com.nexsol.cargo.web.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements JwtService {

	private final JwtConfig jwtConfig;

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String createToken(JwtPayload payload) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

		return Jwts.builder()
			.setSubject(String.valueOf(payload.sub()))
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(getSecretKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public JwtPayload parseToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();

		Long sub = Long.parseLong(claims.getSubject());
		return new JwtPayload(sub);
	}

	@Override
	public String sign(JwtPayload payload) {
		return createToken(payload);
	}

	@Override
	public JwtPayload verify(String token) {
		return parseToken(token);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
			return true;
		}
		catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Long getUserId(String token) {
		return parseToken(token).sub();
	}

}