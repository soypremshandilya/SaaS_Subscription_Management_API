package com.saas.subscription.controller;

import com.saas.subscription.dto.SubscriptionRequest;
import com.saas.subscription.dto.SubscriptionResponse;
import com.saas.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> addSubscription(@Valid @RequestBody SubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.addSubscription(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Subscription deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/spending/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlySpending(@PathVariable Long userId) {
        Double total = subscriptionService.calculateMonthlySpending(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("monthlySpending", total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/spending/yearly")
    public ResponseEntity<Map<String, Object>> getYearlySpending(@PathVariable Long userId) {
        Double total = subscriptionService.calculateYearlySpending(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("yearlySpending", total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/renewals")
    public ResponseEntity<List<SubscriptionResponse>> getUpcomingRenewals(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(subscriptionService.getUpcomingRenewals(userId, days));
    }
}
