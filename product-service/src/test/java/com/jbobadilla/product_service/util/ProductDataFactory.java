package com.jbobadilla.product_service.util;

import com.jbobadilla.product_service.model.dtos.ProductRequest;
import com.jbobadilla.product_service.model.dtos.ProductResponse;
import com.jbobadilla.product_service.model.entities.Product;

public class ProductDataFactory {

    // Muestra el Mock q devolverá
    public static ProductResponse.ProductResponseBuilder getMockProductResponse() {
        return ProductResponse.builder()
                .id(1L)
                .sku("sku1")
                .name("Test Product")
                .description("Test Product")
                .price(100.0)
                .status(true);
    }

    // Muestra el Mock que guardará
    public static ProductRequest.ProductRequestBuilder getMockProductRequest() {
        return ProductRequest.builder()
                .sku("sku1")
                .name("Test Product")
                .description("Test Product")
                .price(100.0)
                .status(true);
    }

    // Objeto Product
    public static Product.ProductBuilder getMockProduct() {
        return Product.builder()
                .id(1L)
                .sku("sku1")
                .name("Test Product")
                .description("Test Product")
                .price(100.0)
                .status(true);
    }
}