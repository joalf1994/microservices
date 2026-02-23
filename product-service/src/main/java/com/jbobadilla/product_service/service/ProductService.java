package com.jbobadilla.product_service.service;

import com.jbobadilla.product_service.model.dtos.ProductRequest;
import com.jbobadilla.product_service.model.dtos.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse findById(Long id);
    List<ProductResponse> findAll();
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
}
