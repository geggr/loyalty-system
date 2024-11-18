package com.grimoire.loyalty.products;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class ProductRepositoryCustom {

    private static final String LOCK_PRODUCT_QUERY = """
        SELECT
            item.*
        FROM
            product_items item
        JOIN
            products product ON product.id = item.product_id
        WHERE
            product.code = :productCode
        AND
            item.available IS TRUE
        LIMIT 1
        FOR UPDATE OF item SKIP LOCKED        
    """;
    
    // query mais otimizada, poderiamos ter ganho de performânce
    // mas iriamos perder algumas informações importantes para
    // construir o dto para o front-end
    @SuppressWarnings("unused")
    private static final String LOCK_PRODUCT_QUERY_FOR_USER_WITH_AVAILABLE_POINTS = """
        SELECT
            item.*
        FROM
            product_items item
        JOIN
            products product ON product.id = item.product_id
        WHERE
            product.code = :productCode
        AND
            product.price <= (
                SELECT SUM(transaction.points)
                FROM customer_point_transactions transaction
                JOIN customers customer ON customer.id = transaction.customer_id
                WHERE customer.email = :email
            )
        LIMIT 1
        FOR UPDATE OF item SKIP LOCKED        
    """;

    private final EntityManager manager;

    public ProductRepositoryCustom(EntityManager manager) {
        this.manager = manager;
    }
    
    @Transactional
    @SuppressWarnings("unchecked")
    public Optional<ProductItem> lockItem(String productCode){
        return manager
            .createNativeQuery(LOCK_PRODUCT_QUERY, ProductItem.class)
            .setParameter("productCode", productCode)
            .getResultStream()
            .findFirst();
    }
}
