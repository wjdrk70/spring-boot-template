package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

	private Long id;

	private String companyCode;

	private String password;

	private UserRole role;

}
