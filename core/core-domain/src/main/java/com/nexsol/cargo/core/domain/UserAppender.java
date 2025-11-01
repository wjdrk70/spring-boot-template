package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserAppender {

	private final UserRepository userRepository;

	private final UserProfileRepository userProfileRepository;

	private final CompanyInfoRepository companyInfoRepository;

	@Transactional
	public User append(User user, String hashedPassword) {
		User newUser = User.builder().loginId(user.getLoginId()).password(hashedPassword).role(user.getRole()).build();

		User savedUser = userRepository.save(newUser);

		UserProfile savedProfile = userProfileRepository.save(savedUser.getId(), user.getProfile());

		CompanyInfo savedCompanyInfo = companyInfoRepository.save(savedUser.getId(), user.getCompanyInfo());

		return User.builder()
			.id(savedUser.getId())
			.loginId(savedUser.getLoginId())
			.password(savedUser.getPassword())
			.role(savedUser.getRole())
			.profile(savedProfile)
			.companyInfo(savedCompanyInfo)
			.build();
	}

}
