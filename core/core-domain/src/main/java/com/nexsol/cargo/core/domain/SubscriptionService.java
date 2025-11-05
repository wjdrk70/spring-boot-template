package com.nexsol.cargo.core.domain;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
 private final SubscriptionRepository subscriptionRepository;

 public Long complete(Subscription subscription){
     Subscription savedSubscription = subscriptionRepository.save(subscription); // [수정] SubscriptionCommand -> Subscription

     // ...

     return savedSubscription.id();
 }
}
