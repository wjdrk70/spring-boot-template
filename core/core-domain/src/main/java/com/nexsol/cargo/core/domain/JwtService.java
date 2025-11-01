package com.nexsol.cargo.core.domain;

public interface JwtService {

	String sign(JwtPayload payload);

	JwtPayload verify(String token);

	boolean validateToken(String token);

	Long getUserId(String token);

}