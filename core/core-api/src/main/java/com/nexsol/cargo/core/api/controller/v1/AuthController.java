package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.SignUpRequest;
import com.nexsol.cargo.core.domain.AuthService;
import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.api.controller.v1.response.SignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ApiResponse<SignUpResponse> register(@Valid @RequestBody SignUpRequest request) {
		User user = request.toDomain();
		User createUser = authService.signUp(user);
		return ApiResponse.success(SignUpResponse.fromDomain(createUser));
	}

}
