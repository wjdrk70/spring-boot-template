package com.nexsol.cargo.core.api.controller.v1.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PlanRequest(@NotBlank(message = "가견적 Key는 필수입니다.") String quotationKey) {

}
