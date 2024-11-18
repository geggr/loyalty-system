package com.grimoire.loyalty.products.output;

import java.util.List;

public record CreatedProductStockResponse(String productCode, List<ProductItemDTO> stock) {
    
}
