package com.grimoire.loyalty.products.output;

import com.grimoire.loyalty.products.ProductItem;

public record ProductItemDTO(String id, String sku) {
    public ProductItemDTO(ProductItem item){
        this(item.getUuid().toString(), item.getSku());
    }
}
