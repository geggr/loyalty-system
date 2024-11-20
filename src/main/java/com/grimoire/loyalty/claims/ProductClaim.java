package com.grimoire.loyalty.claims;

import java.time.LocalDateTime;
import java.util.UUID;

import com.grimoire.loyalty.customers.Customer;
import com.grimoire.loyalty.customers.CustomerPointTransaction;
import com.grimoire.loyalty.products.Product;
import com.grimoire.loyalty.products.ProductItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_claims")
public class ProductClaim {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id", nullable = true)
    private ProductItem item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private CustomerPointTransaction transaction;
    
    @Column(name = "claim_at")
    private LocalDateTime claimDate;

    @Column(name = "redeem_at")
    private LocalDateTime redeemAt;

    @Deprecated // hibernate eyes
    public ProductClaim(){}

    public ProductClaim(Customer customer, Product product, CustomerPointTransaction transaction) {
        this.customer = customer;
        this.transaction = transaction;
        this.product = product;
        this.claimDate = transaction.getTransactionDate();
    }

    public ProductClaim(Customer customer, ProductItem item, CustomerPointTransaction transaction) {
        this(customer, item.getProduct(), transaction);
        this.item = item;
    }

    public boolean alreadyRedeemed(){
        return redeemAt != null;
    }

    public boolean hasProductId(){
        return item != null;
    }

    public void redeem(){
        this.redeemAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public ProductItem getItem() {
        return item;
    }

    public CustomerPointTransaction getTransaction() {
        return transaction;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public LocalDateTime getRedeemAt() {
        return redeemAt;
    }
}
