package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

	private final UserRepository userRepository;

	public User read(String companyCode) {
		return userRepository.findByCompanyCode(companyCode)
			.orElseThrow(() -> new CoreException(CoreErrorType.USER_NOT_FOUND));
	}

	public void findUser(String companyCode) {
		userRepository.findByCompanyCode(companyCode).ifPresent(user -> {
			throw new CoreException(CoreErrorType.USER_EXIST_DATA);
		});
	}

}
