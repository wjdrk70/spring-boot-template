package com.nexsol.cargo.core.domain;

import java.util.Optional;

public interface UserRepository {

	User save(User user);

	Optional<User> findByCompanyCode(String companyCode);

	Optional<User> findById(Long id);

}
