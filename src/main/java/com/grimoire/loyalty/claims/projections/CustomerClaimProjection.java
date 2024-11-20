package com.grimoire.loyalty.claims.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CustomerClaimProjection {
    UUID getId();
    String getProductName();
    String getSku();
    LocalDateTime getClaimAt();
}
