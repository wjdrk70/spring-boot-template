package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserReader userReader;

	private final UserAppender userAppender;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	public User signUp(User user) {
		userReader.findUser(user.getCompanyCode());

		String hashedPassword = passwordEncoder.encode(user.getPassword());

		User newUser = userAppender.append(user, hashedPassword);

		return newUser;
	}

	public String signIn(String companyCode, String password) {

		User existUser = userReader.read(companyCode);

		boolean equalPassword = passwordEncoder.matches(password, existUser.getPassword());

		if (!equalPassword) {
			throw new CoreException(CoreErrorType.AUTH_UNAUTHORIZED);
		}

		JwtPayload payload = new JwtPayload(existUser.getId());
		return jwtService.sign(payload);
	}

}
