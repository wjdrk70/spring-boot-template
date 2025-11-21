package com.nexsol.cargo.core.domain;

public interface BucketStorageClient {

	/**
	 * 서명 이미지 데이터(바이트 배열)를 저장소(MinIO)에 업로드하고 고유 키를 반환합니다.
	 * @param subscriptionId 청약 ID (파일 키/경로에 사용)
	 * @param imageBytes 서명 이미지의 바이트 배열
	 * @param contentType 이미지의 MIME 타입 (예: "image/png")
	 * @return 저장된 파일의 고유 키 (MinIO Object Key)
	 */
	String uploadSignature(Long subscriptionId, byte[] imageBytes, String contentType);

	/**
	 * @param signatureKey 저장된 MinIO 객체 키
	 * @return 서명 이미지의 바이트 배열
	 */
	byte[] downloadSignature(String signatureKey);

	/**
	 * presigned url 방식
	 * @param signatureKey MinIO 객체 키 (DB에 저장된 값)
	 * @return 10분 등 제한된 시간 동안 유효한 다운로드 URL
	 *
	 */
	String generateDownloadPresignedUrl(String signatureKey);

}
