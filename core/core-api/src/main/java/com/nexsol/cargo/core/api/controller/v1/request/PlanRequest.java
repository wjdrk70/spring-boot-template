package com.nexsol.cargo.core.api.controller.v1.request;

import jakarta.validation.constraints.NotBlank;

public record PlanRequest(@NotBlank(message = "가견적 Key는 필수입니다.") String quotationKey) {

}
