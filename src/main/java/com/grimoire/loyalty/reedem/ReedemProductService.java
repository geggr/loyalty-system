package com.grimoire.loyalty.reedem;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grimoire.loyalty.claims.ProductClaim;
import com.grimoire.loyalty.customers.Customer;
import com.grimoire.loyalty.customers.CustomerPointTransaction;
import com.grimoire.loyalty.customers.CustomerService;
import com.grimoire.loyalty.products.Product;
import com.grimoire.loyalty.products.ProductRepositoryCustom;
import com.grimoire.loyalty.reedem.input.ReedemProductRequest;
import com.grimoire.loyalty.utils.errors.BadRequestError;
import com.grimoire.loyalty.utils.errors.RequestFieldError;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class ReedemProductService {
    
    private final CustomerService customerService;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final EntityManager manager;
    
    public ReedemProductService(CustomerService customerService, ProductRepositoryCustom productRepositoryCustom, EntityManager manager) {
        this.customerService = customerService;
        this.productRepositoryCustom = productRepositoryCustom;
        this.manager = manager;
    }

    @Transactional
    public ProductClaim execute(ReedemProductRequest request){
        final var product = manager
            .createQuery("SELECT p from Product p WHERE p.code = :code", Product.class)
            .setParameter("code", request.productCode())
            .getResultStream()
            .findFirst()
            .orElseThrow(() -> new BadRequestError("Product not found for reedem"));

        final var customer = customerService
            .fetchCustomerWithTransactions(request.customerEmail())
            .orElseThrow(() -> new BadRequestError("Customer does not exists"));

        if (!customer.canAcquire(product)){
            throw new BadRequestError("Customer can not acquire this product", "insufficient_founds", "Customer has %s points and product cost %s".formatted(customer.getCurrentTotalPoints(), product.getPrice()));
        }

        return product.isLimited()
            ? reedemFromStock(customer, product)
            : reedemVirtualProduct(customer, product);
    }

    private ProductClaim reedemVirtualProduct(Customer customer, Product product){
        final var transaction = CustomerPointTransaction.ofProductClaim(customer, product, "Claim product".formatted(product.getName()));
        customer.addTransaction(transaction);
        
        final var claim = new ProductClaim(customer, transaction);
        
        manager.persist(transaction);
        manager.persist(claim);

        return claim;
    }

    private ProductClaim reedemFromStock(Customer customer, Product product){
        final var lockedItem = productRepositoryCustom.lockItem(product.getCode());

        if (lockedItem.isEmpty()){
            throw new BadRequestError(
                "Failed to reedem product. Try again later!",
                List.of(new RequestFieldError("empty_stock", "Item not found in stock")));
        }

        final var item = lockedItem.get();
        item.consume();

        final var transaction = CustomerPointTransaction.ofProductClaim(customer, product, "Claim product".formatted(product.getName()));
        customer.addTransaction(transaction);
        
        final var claim = new ProductClaim(customer, item, transaction);

        manager.persist(item);
        manager.persist(transaction);
        manager.persist(claim);

        return claim;
    }
}
