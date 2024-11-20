package com.grimoire.loyalty.claims;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grimoire.loyalty.claims.input.ReedemCustomerClaim;
import com.grimoire.loyalty.claims.output.SuccessProductClaim;
import com.grimoire.loyalty.claims.projections.CustomerClaimProjection;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/claims")
public class ProductClaimController {

    private final ProductClaimService service;

    public ProductClaimController(ProductClaimService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerClaimProjection>> handleFetchUserClaim(@PathParam("email") String email){
        return ResponseEntity.ok(service.fetch(email));
    }

    @PostMapping("/execute")
    public ResponseEntity<SuccessProductClaim> handleExecuteClaim(@RequestBody ReedemCustomerClaim request){
        final var claim = service.redeem(request.id());
        final var response = new SuccessProductClaim(claim.getUuid().toString(), claim.getRedeemAt());
        return ResponseEntity.ok(response);
    }
}
