package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppender {

	private final UserRepository userRepository;

	public User append(User user, String hashedPassword) {

		User userToSave = User.builder()
			.companyCode(user.getCompanyCode())
			.password(hashedPassword) // ⬅️ 해시된 비밀번호 사용
			.role(user.getRole())
			.profile(user.getProfile())
			.build();

		User savedUser = userRepository.save(userToSave);

		return savedUser;
	}

}
