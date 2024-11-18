package com.grimoire.loyalty.products.input;

import org.hibernate.validator.constraints.Length;

import com.grimoire.loyalty.products.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateProductRequest {

    @NotNull @Length(min = 3, max = 30)
    private String code;

    @NotNull @NotBlank
    private String name;

    @NotNull @Positive
    private Integer price;
    
    @NotNull
    private boolean limited;

    private String imageUrl;
    
    @Deprecated // spring eyes
    public CreateProductRequest(){}

    public Product toEntity(){
        return new Product(code, name, price, limited);
    }

	public String getCode() {
		return code;
	}

    public Integer getPrice() {
        return price;
    }

	public String getName() {
		return name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean isLimited() {
		return limited;
	}
}
