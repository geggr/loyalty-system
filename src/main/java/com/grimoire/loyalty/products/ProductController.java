package com.grimoire.loyalty.products;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grimoire.loyalty.products.input.CreateProductRequest;
import com.grimoire.loyalty.products.input.CreateProductStockRequest;
import com.grimoire.loyalty.products.output.CreatedProductResponse;
import com.grimoire.loyalty.products.output.CreatedProductStockResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final ProductRepository repository;

    public ProductController(ProductService service, ProductRepository repository) {
		this.service = service;
        this.repository = repository;
	}

	@PostMapping
    public ResponseEntity<CreatedProductResponse> handleCreateProduct(@RequestBody @Valid CreateProductRequest request){
        final var product = repository.save(request.toEntity());
        return ResponseEntity.ok(new CreatedProductResponse("Successfully Created Product!", product.getCode()));
    }

    @PostMapping("{code}/stock")
    public ResponseEntity<CreatedProductStockResponse> handleIncreaseProductStock(@PathVariable("code") String code, @RequestBody @Valid CreateProductStockRequest request){
        final var stock = service.createStockForProduct(code, request);
        return ResponseEntity.ok(new CreatedProductStockResponse(code, stock));
    }
}
