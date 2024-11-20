package com.grimoire.loyalty.claims;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.grimoire.loyalty.claims.projections.CustomerClaimProjection;
import com.grimoire.loyalty.utils.errors.BadRequestError;
import com.grimoire.loyalty.utils.errors.NotFoundError;

@Service
public class ProductClaimService {
    
    private final ProductClaimRepository repository;

    public ProductClaimService(ProductClaimRepository repository) {
        this.repository = repository;
    }

    public List<CustomerClaimProjection> fetch(String email){
        return repository.fetchAvaiableClaimsForCustomer(email);
    }

    public ProductClaim redeem(String id){
        final var claim = repository
            .findByUuid(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundError("Claim not found!"));
        
        if (claim.alreadyRedeemed()){
            throw new BadRequestError("Bad Request", "already_reedemed", "This claim has already been redeemed");
        }
        
        claim.redeem();
        repository.save(claim);
        return claim;
    }
    
}
