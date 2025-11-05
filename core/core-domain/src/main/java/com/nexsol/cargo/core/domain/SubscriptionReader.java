package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.enums.SubscriptionStatus;
import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription read(Long subscriptionId, Long userId) {
        // (구현 예시)
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new CoreException(CoreErrorType.NOT_FOUND_DATA));


        if (!subscription.userId().equals(userId)) {

            throw new CoreException(CoreErrorType.AUTH_UNAUTHORIZED);
        }


        if (subscription.status() != SubscriptionStatus.PAYMENT_PENDING) {

        }

        return subscription;
    }
}
