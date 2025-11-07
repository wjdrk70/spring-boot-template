package com.nexsol.cargo.core.domain;

/**
 * PG사 승인(approve) 요청의 성공 결과
 *
 * @param authCode PG사 승인 번호
 * @param cardCode PG사 카드 코드 (e.g., "01")
 */
public record PgApprovalResult(String authCode, String cardCode) {
}