package com.nexsol.cargo.core.api.controller.v1.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PlanRequest(
        @NotBlank(message = "HS Code는 필수입니다.")
        String hsCode,

        @NotNull(message = "송장가액은 필수입니다.")
        @DecimalMin(value = "0.0", inclusive = false, message = "송장가액은 0보다 커야 합니다.")
        BigDecimal invoiceAmount

//        @NotNull(message = "Ref No 는 필수 입니다.")
//        String referenceNo,
//
//        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // yyyy-MM-dd
//        LocalDate outboundDate, // 출발일자
//
//        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//        LocalDate inboundDate, // 도착일자
//
//        @NotNull(message = "출발지는 필수입니다.")
//        String origin, // 출발지
//
//        @NotNull(message = "도착지 필수입니다.")
//        String destination, // 도착지
//
//        @NotNull(message = "운송용구는 필수입니다.")
//        String conveyance, // 운송용구
//
//        @NotNull(message = "환율 단위는 필수입니다.")
//        String currency, // 화폐단위
//
//        @NotNull(message = "B/L no 는 필수입니다.")
//        String billNo, // B/L No.
//
//        @NotNull(message = "포장 방법은 필수입니다.")
//        String packingList, // 포장
//
//        @NotNull(message = "품목상세는 필수입니다.")
//        String cargoItem // 품목상세


) {
//    public PlanOCR toDomain() {
//        return new PlanOCR(
//                this.hsCode,
//                this.invoiceAmount,
//                this.referenceNo,
//                this.outboundDate,
//                this.inboundDate,
//                this.origin,
//                this.destination,
//                this.conveyance,
//                this.currency,
//                this.billNo,
//                this.packingList,
//                this.cargoItem
//        );
//    }

}
