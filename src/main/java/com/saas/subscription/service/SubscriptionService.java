package com.saas.subscription.service;

import com.saas.subscription.dto.SubscriptionRequest;
import com.saas.subscription.dto.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse addSubscription(SubscriptionRequest request);
    List<SubscriptionResponse> getAllSubscriptions(Long userId);
    SubscriptionResponse updateSubscription(Long id, SubscriptionRequest request);
    void deleteSubscription(Long id);
    
    Double calculateMonthlySpending(Long userId);
    Double calculateYearlySpending(Long userId);
    List<SubscriptionResponse> getUpcomingRenewals(Long userId, int days);
}
