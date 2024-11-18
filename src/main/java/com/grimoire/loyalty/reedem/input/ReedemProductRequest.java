package com.grimoire.loyalty.reedem.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReedemProductRequest(@JsonProperty("customer_email") String customerEmail, @JsonProperty("product_code") String productCode) {
    
}
