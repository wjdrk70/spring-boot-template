package com.nexsol.cargo.client.bucket.storage;

import com.nexsol.cargo.client.bucket.storage.config.MinioProperties;
import com.nexsol.cargo.core.domain.BucketStorageClient;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BucketService implements BucketStorageClient {

	private final S3Client s3Client;

	private final S3Presigner s3Presigner;

	private final MinioProperties minioProperties;

	@Override
	public String uploadSignature(Long subscriptionId, byte[] imageBytes, String contentType) {
		String extension = getExtensionFromContentType(contentType);
		String objectKey = String.format("signatures/%d/%s.%s", subscriptionId, UUID.randomUUID().toString(),
				extension);
		try {
			// S3 PutObject 요청 생성
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(minioProperties.getBucketName())
				.key(objectKey)
				.contentType(contentType)
				.contentLength((long) imageBytes.length)
				.build();

			s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));

			return objectKey;

		}
		catch (Exception e) {
			// TODO: CoreException
			throw new CoreException(CoreErrorType.SUBSCRIPTION_SIGNATURE_UPLOAD_FAILED);
		}
	}

	@Override
	public byte[] downloadSignature(String signatureKey) {
		try {

			GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(minioProperties.getBucketName())
				.key(signatureKey)
				.build();

			ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

			return s3Object.readAllBytes();

		}
		catch (Exception e) {
			throw new CoreException(CoreErrorType.SUBSCRIPTION_SIGNATURE_DOWNLOAD_FAILED);
		}
	}

	@Override
	public String generateDownloadPresignedUrl(String signatureKey) {
		try {

			GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(minioProperties.getBucketName())
				.key(signatureKey)
				.build();

			GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
				.getObjectRequest(getObjectRequest)
				.signatureDuration(Duration.ofMinutes(10))
				.build();

			return s3Presigner.presignGetObject(presignRequest).url().toString();

		}
		catch (Exception e) {

			throw new CoreException(CoreErrorType.SUBSCRIPTION_PRESIGNED_URL_FAILED);
		}
	}

	private String getExtensionFromContentType(String contentType) {
		if (contentType == null || !contentType.startsWith("image/")) {
			return "bin";
		}

		String[] parts = contentType.split("/");
		return (parts.length > 1) ? parts[1].replace("jpeg", "jpg") : "bin"; // jpeg를 jpg로

	}

}
