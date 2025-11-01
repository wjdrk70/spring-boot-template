package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

	private final UserRepository userRepository;

	public User read(String id) {
		return userRepository.findByLonginId(id).orElseThrow(() -> new CoreException(CoreErrorType.USER_NOT_FOUND));
	}

	public void findUser(String id) {
		userRepository.findByLonginId(id).ifPresent(user -> {
			throw new CoreException(CoreErrorType.USER_EXIST_DATA);
		});
	}

}
