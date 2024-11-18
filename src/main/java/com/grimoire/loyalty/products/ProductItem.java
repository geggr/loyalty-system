package com.grimoire.loyalty.products;

import java.util.UUID;

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
@Table(name = "product_items")
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sku")
    private String sku;

    private boolean available;

    @Deprecated // hibernate eyes
    public ProductItem(){}

    public ProductItem(Product product, String sku, boolean available){
        this.product = product;
        this.sku = sku;
        this.available = available;
    }

    public void consume() {
        this.available = false;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Product getProduct() {
        return product;
    }

    public String getSku() {
        return sku;
    }

    public boolean isAvailable() {
        return available;
    }
}
