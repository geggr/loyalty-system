package com.grimoire.loyalty.products;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "products")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    private Integer price;

    private boolean limited;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductItem> stock = new ArrayList<>();

    @Deprecated // hibernate eyes
    public Product(){}

    public Product(String code, String name, Integer price, boolean limited){
        this.code = code;
        this.name = name;
        this.price = price;
        this.limited = limited;
    }

    public Long getId() {
        return id;
    }
    
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isLimited() {
        return limited;
    }

    public boolean isUnlimited(){
        return !limited;
    }
}
