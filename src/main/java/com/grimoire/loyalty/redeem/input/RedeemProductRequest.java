package com.grimoire.loyalty.redeem.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RedeemProductRequest(@JsonProperty("customer_email") String customerEmail, @JsonProperty("product_code") String productCode) {
    
}
