package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserReader userReader;

	private final UserRepository userRepository;

	private final UserProfileRepository userProfileRepository;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	public UserInfo signUp(User user, UserProfile profile) {
		userReader.findUser(user.getCompanyCode());
		String hashedPassword = passwordEncoder.encode(user.getPassword());

		User userToSave = User.builder()
			.companyCode(user.getCompanyCode())
			.password(hashedPassword)
			.role(user.getRole())
			.build();
		User savedUser = userRepository.save(userToSave);

		UserProfile profileToSave = UserProfile.builder()
			.userId(savedUser.getId())
			.userName(profile.getUserName())
			.companyName(profile.getCompanyName())
			.managerName(profile.getManagerName())
			.phoneNumber(profile.getPhoneNumber())
			.email(profile.getEmail())
			.address(profile.getAddress())
			.build();
		UserProfile savedProfile = userProfileRepository.save(profileToSave);

		return new UserInfo(null, savedUser, savedProfile);
	}

	public UserInfo signIn(String companyCode, String password) {

		User existUser = userReader.read(companyCode);

		boolean equalPassword = passwordEncoder.matches(password, existUser.getPassword());

		if (!equalPassword) {
			throw new CoreException(CoreErrorType.AUTH_UNAUTHORIZED);
		}

		UserProfile profile = userReader.readProfile(existUser.getId());

		JwtPayload payload = new JwtPayload(existUser.getId());
		String token = jwtService.sign(payload);

		return new UserInfo(token, existUser, profile);
	}

}
