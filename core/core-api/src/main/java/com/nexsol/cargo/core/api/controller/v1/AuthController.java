package com.nexsol.cargo.core.api.controller.v1;

import com.nexsol.cargo.core.api.controller.v1.request.SignInRequest;
import com.nexsol.cargo.core.api.controller.v1.request.SignUpRequest;
import com.nexsol.cargo.core.api.controller.v1.response.SignInResponse;
import com.nexsol.cargo.core.domain.AuthService;
import com.nexsol.cargo.core.domain.User;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import com.nexsol.cargo.core.api.controller.v1.response.SignUpResponse;
import com.nexsol.cargo.core.domain.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ApiResponse<SignInResponse> login(@RequestBody SignInRequest request) {
        UserInfo result = authService.signIn(request.getCompanyCode(), request.getPassword());
        return ApiResponse.success(SignInResponse.fromDomain(result.accessToken(), result.user()));
    }

}
