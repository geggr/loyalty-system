package com.grimoire.loyalty.products;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.grimoire.loyalty.products.input.CreateProductStockRequest;
import com.grimoire.loyalty.products.output.ProductItemDTO;
import com.grimoire.loyalty.utils.errors.BadRequestError;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductItemRepository itemRepository;

    public ProductService(ProductRepository repository, ProductItemRepository itemRepository) {
		this.productRepository = repository;
        this.itemRepository = itemRepository;
	}

	public List<ProductItemDTO> createStockForProduct(String productCode, CreateProductStockRequest request){
        final var product = productRepository
            .findByCode(productCode)
            .orElseThrow(
                () -> new BadRequestError("Validation Error", "product_not_found", "The product do not exists")
            );

        if (product.isUnlimited()){
            throw new BadRequestError("Validation Error", "unlimited_product", "The product is unlimited, can not have stock");
        }

        final var isInitialAvaiable = request.isInitialAvaiable();

        final var items = IntStream
            .range(0, request.getTotal())
            .mapToObj(_ -> new ProductItem(product, ProductSkuGenerator.generate(), isInitialAvaiable))
            .toList();
        
        itemRepository.saveAll(items);

        return items.stream().map(ProductItemDTO::new).toList();
    }
}
