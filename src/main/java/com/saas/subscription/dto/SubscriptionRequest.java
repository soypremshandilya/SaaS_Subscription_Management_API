package com.saas.subscription.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.01", message = "Cost must be greater than 0")
    private Double cost;

    @NotBlank(message = "Billing cycle is required")
    @Pattern(regexp = "^(MONTHLY|YEARLY)$", message = "Billing cycle must be either MONTHLY or YEARLY")
    private String billingCycle;

    @NotNull(message = "Renewal date is required")
    @FutureOrPresent(message = "Renewal date must be in the present or future")
    private LocalDate renewalDate;
}
