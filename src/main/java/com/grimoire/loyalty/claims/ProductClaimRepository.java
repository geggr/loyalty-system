package com.grimoire.loyalty.claims;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.grimoire.loyalty.claims.projections.CustomerClaimProjection;

@Repository
public interface ProductClaimRepository extends JpaRepository<ProductClaim, Long> {
    
    @Query(
        value = """
            SELECT
                claim.uuid as id,
                product.name as productName,
                item.sku as sku,
                claim.claim_at as claimAt
            FROM
                product_claims claim
            JOIN
                customers customer ON customer.id = claim.customer_id
            JOIN
                products product ON product.id = claim.product_id
            LEFT JOIN
                product_items item ON item.id = claim.product_item_id
            WHERE
                customer.email = :email
            AND
                claim.redeem_at IS NULL
        """,
        nativeQuery = true
    )
    List<CustomerClaimProjection> fetchAvaiableClaimsForCustomer(String email);

    Optional<ProductClaim> findByUuid(UUID id);
}
