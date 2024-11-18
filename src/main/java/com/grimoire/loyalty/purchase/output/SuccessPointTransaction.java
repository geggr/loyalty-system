package com.grimoire.loyalty.purchase.output;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SuccessPointTransaction(
    @JsonProperty("customer_email") String customerEmail,
    @JsonProperty("total_points") int totalPoints
) {}
