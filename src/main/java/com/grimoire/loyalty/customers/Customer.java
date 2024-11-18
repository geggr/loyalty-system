package com.grimoire.loyalty.customers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.grimoire.loyalty.products.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid = UUID.randomUUID();

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CustomerPointTransaction> transactions = new ArrayList<>();

    @Deprecated // hibernate eyes
    public Customer(){}

    public Customer(String email){
        this.email = email;
    }
    
    public void addTransaction(CustomerPointTransaction transaction) {
        this.transactions.add(transaction);
    }

    public boolean canAcquire(Product product) {
        System.out.println(getCurrentTotalPoints());
        return getCurrentTotalPoints() >= product.getPrice();
    }

    public int getCurrentTotalPoints() {
        return transactions.stream().mapToInt(CustomerPointTransaction::getPoints).sum();
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public List<CustomerPointTransaction> getTransactions() {
        return transactions;
    }
}
