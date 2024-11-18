package com.grimoire.loyalty.products.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateProductStockRequest {
    
    @NotNull @Positive
    private int total;

    @JsonProperty("initial_available")
    @NotNull
    private boolean initialAvaiable;

    @Deprecated
    public CreateProductStockRequest(){}

    public int getTotal() {
        return total;
    }

    public boolean isInitialAvaiable() {
        return initialAvaiable;
    }
}
