package com.saas.subscription.service.impl;

import com.saas.subscription.dto.SubscriptionRequest;
import com.saas.subscription.dto.SubscriptionResponse;
import com.saas.subscription.exception.ResourceNotFoundException;
import com.saas.subscription.model.Subscription;
import com.saas.subscription.model.User;
import com.saas.subscription.repository.SubscriptionRepository;
import com.saas.subscription.repository.UserRepository;
import com.saas.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public SubscriptionResponse addSubscription(SubscriptionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Subscription subscription = Subscription.builder()
                .user(user)
                .serviceName(request.getServiceName())
                .cost(request.getCost())
                .billingCycle(request.getBillingCycle())
                .renewalDate(request.getRenewalDate())
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return mapToResponse(savedSubscription);
    }

    @Override
    public List<SubscriptionResponse> getAllSubscriptions(Long userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return subscriptionRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponse updateSubscription(Long id, SubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));

        // Note: we can optionally allow updating the user, but typically a subscription belongs to the same user.
        // We ensure the user is still valid if passed, else retain old.
        if (!subscription.getUser().getId().equals(request.getUserId())) {
            User newUser = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            subscription.setUser(newUser);
        }

        subscription.setServiceName(request.getServiceName());
        subscription.setCost(request.getCost());
        subscription.setBillingCycle(request.getBillingCycle());
        subscription.setRenewalDate(request.getRenewalDate());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return mapToResponse(updatedSubscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
        subscriptionRepository.delete(subscription);
    }

    @Override
    public Double calculateMonthlySpending(Long userId) {
        List<Subscription> subscriptions = getSubscriptionsByUserId(userId);
        
        return subscriptions.stream()
                .mapToDouble(sub -> {
                    if ("YEARLY".equalsIgnoreCase(sub.getBillingCycle())) {
                        return sub.getCost() / 12.0;
                    }
                    return sub.getCost(); // MONTHLY
                })
                .sum();
    }

    @Override
    public Double calculateYearlySpending(Long userId) {
        List<Subscription> subscriptions = getSubscriptionsByUserId(userId);
        
        return subscriptions.stream()
                .mapToDouble(sub -> {
                    if ("MONTHLY".equalsIgnoreCase(sub.getBillingCycle())) {
                        return sub.getCost() * 12.0;
                    }
                    return sub.getCost(); // YEARLY
                })
                .sum();
    }

    @Override
    public List<SubscriptionResponse> getUpcomingRenewals(Long userId, int days) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        return subscriptionRepository.findByUserIdAndRenewalDateBetween(userId, today, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private List<Subscription> getSubscriptionsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return subscriptionRepository.findByUserId(userId);
    }

    private SubscriptionResponse mapToResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .serviceName(subscription.getServiceName())
                .cost(subscription.getCost())
                .billingCycle(subscription.getBillingCycle())
                .renewalDate(subscription.getRenewalDate())
                .build();
    }
}
