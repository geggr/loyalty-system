package com.grimoire.loyalty.reedem.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grimoire.loyalty.claims.ProductClaim;

public record SuccessReedemProductResponse(String message, String sku, @JsonProperty("remaining_points") Integer remainingPoints) {
    public SuccessReedemProductResponse(String message, ProductClaim claim){
        this(
            message,
            claim.hasProductId() ? claim.getItem().getSku() : null,
            claim.getCustomer().getCurrentTotalPoints()
        );
    }
}
