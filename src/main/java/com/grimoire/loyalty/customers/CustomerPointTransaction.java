package com.grimoire.loyalty.customers;

import java.time.LocalDateTime;

import com.grimoire.loyalty.products.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_point_transactions")
public class CustomerPointTransaction {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "points")
    private Integer points;

    private String description;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Deprecated // hibernate only
    public CustomerPointTransaction(){}

    public CustomerPointTransaction(Customer customer, Integer points, String description, LocalDateTime transactionDate){
        this.customer = customer;
        this.points = points;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public static CustomerPointTransaction ofProductClaim(Customer customer, Product product, String description){
        return new CustomerPointTransaction(customer, (-1 * product.getPrice()), description, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Integer getPoints() {
        return points;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}
