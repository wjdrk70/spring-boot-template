package com.nexsol.cargo.core.domain;

public interface UserProfileRepository {

	UserProfile save(Long userId, UserProfile profile);

}
