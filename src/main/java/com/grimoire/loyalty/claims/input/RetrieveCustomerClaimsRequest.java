package com.grimoire.loyalty.claims.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RetrieveCustomerClaimsRequest(@NotNull @Email String email) {
    
}
