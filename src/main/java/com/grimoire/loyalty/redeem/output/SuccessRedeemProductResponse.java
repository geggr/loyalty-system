package com.grimoire.loyalty.redeem.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grimoire.loyalty.claims.ProductClaim;

public record SuccessRedeemProductResponse(String message, String sku, @JsonProperty("remaining_points") Integer remainingPoints) {
    public SuccessRedeemProductResponse(String message, ProductClaim claim){
        this(
            message,
            claim.hasProductId() ? claim.getItem().getSku() : null,
            claim.getCustomer().getCurrentTotalPoints()
        );
    }
}
