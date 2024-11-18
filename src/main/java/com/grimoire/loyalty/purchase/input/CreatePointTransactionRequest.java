package com.grimoire.loyalty.purchase.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePointTransactionRequest(
    @JsonProperty("customer") String customerEmail,
    @JsonProperty("amount") Long amount,
    @JsonProperty("date") String date
){}
