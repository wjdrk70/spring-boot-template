package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import com.nexsol.cargo.core.support.DomainPage;
import com.nexsol.cargo.core.support.DomainPageRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

	private final SubscriptionAppender subscriptionAppender;

	private final SubscriptionReader subscriptionReader;

	private final SubscriptionWriter subscriptionWriter;

	private final SubscriptionContractMapper contractMapper;

	private final SubscriptionSearcher subscriptionSearcher;

	private final QuotationReader quotationReader;

	private final CoverageMasterReader coverageMasterReader;

	private final SubscriptionCoverageManager subscriptionCoverageManager;

	private final PremiumCalculator premiumCalculator;

	private final BucketStorageClient bucketStorageClient;

	@Transactional
	public SubscriptionResult create(CreateSubscription creation) {
		Quotation quotation = quotationReader.read(creation.quotationKey());

		CoverageMaster masterSet = coverageMasterReader.read(creation.coverageCodes());

		List<SubscriptionCoverage> coveragesToSave = subscriptionCoverageManager.create(creation.coverageCodes());

		CargoDetail cargoDetail = quotation.toCargoDetail();

		BigDecimal finalPremium = premiumCalculator.calculate(cargoDetail, quotation.getExchangeRateAmount(),
				masterSet.baseCoverage(), masterSet.options());

		Subscription savedSubscription = subscriptionAppender.append(creation, quotation, coveragesToSave,
				finalPremium);

		return new SubscriptionResult(savedSubscription.getId(), savedSubscription.getInsurancePremium());
	}

	@Transactional
	public Subscription issuePolicy(Long userId, Long subscriptionId) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		if (subscription.getStatus() == SubscriptionStatus.POLICY_ISSUED) {
			return subscription;
		}

		// if (subscription.getStatus() != SubscriptionStatus.PAYMENT_COMPLETE) {
		// throw new CoreException(CoreErrorType.POLICY_CANNOT_BE_ISSUED);
		// }

		String policyNumber = String.format("DB-%d-%06d", LocalDate.now().getYear(), subscription.getId());

		subscription.issuePolicy(policyNumber);

		Subscription issuedSubscription = subscriptionWriter.write(subscription);

		return issuedSubscription;
	}

	@Transactional
	public void saveSignature(Long userId, Long subscriptionId, String signatureBase64, String contentType) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		subscription.saveSignatureBase64Temp(signatureBase64, contentType);

		subscriptionWriter.write(subscription);

	}

	public SubscriptionSignatureImage getSignatureImage(Long userId, Long subscriptionId) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		String signatureKey = subscription.getSignatureKey();

		if (signatureKey == null || signatureKey.isBlank()) {
			throw new CoreException(CoreErrorType.SUBSCRIPTION_NOT_FOUND_SIGNATURE);
		}

		String contentType = subscription.getSignatureContentTypeTemp(); // TODO: 영구 필드에서
																			// 읽도록 수정 필요

		byte[] imageBytes = bucketStorageClient.downloadSignature(signatureKey);

		return new SubscriptionSignatureImage(imageBytes, contentType);
	}

	public String getSignatureDownloadUrl(Long userId, Long subscriptionId) {
		Subscription subscription = subscriptionReader.read(subscriptionId, userId);

		String signatureKey = subscription.getSignatureKey();

		if (signatureKey == null || signatureKey.isBlank()) {
			throw new CoreException(CoreErrorType.NOT_FOUND_DATA);
		}

		return bucketStorageClient.generateDownloadPresignedUrl(signatureKey);
	}

	public SubscriptionSummery<SubscriptionContract> getMyContracts(Long userId, DomainPageRequest pageRequest) {

		SubscriptionSummery<Subscription> entityResult = subscriptionReader.readAllByUserId(userId, pageRequest);

		return contractMapper.map(entityResult);
	}

	public SubscriptionSummery<SubscriptionContract> searchMyContracts(Long userId, SubscriptionSearch contract,
			DomainPageRequest pageRequest) {

		SubscriptionSummery<Subscription> entityResult = subscriptionSearcher.search(userId, contract, pageRequest);

		return contractMapper.map(entityResult);
	}

	public Subscription getSubscription(Long userId, Long subscriptionId) {

		return subscriptionReader.read(subscriptionId, userId);
	}

}
