package com.nexsol.cargo.core.domain;

import java.util.Optional;

public interface UserProfileRepository {

	UserProfile save(UserProfile profile);

	Optional<UserProfile> findByUserId(Long userId);

}
